package com.utp.hexagonal.Infraestructura.Persistencia;

import com.utp.hexagonal.Dominio.modelo.ReporteVentasTrabajador;
import com.utp.hexagonal.Dominio.puertos.salida.ReporteRepository;
import com.utp.hexagonal.Infraestructura.Repository.PedidoRepository; // Necesitamos acceso al repo de Pedidos
import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity; // Necesitamos la entidad PedidoEntity
import com.utp.hexagonal.Infraestructura.Entity.DetallePedidoEntity; // Necesitamos la entidad DetallePedidoEntity

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime; // Para convertir LocalDate a LocalDateTime
import java.time.LocalTime; // Para manejar la parte de la hora en el rango de fechas
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Adaptador de persistencia para los reportes, implementa el ReporteRepository del dominio.
@Repository
public class ReporteJPAAdapter implements ReporteRepository {

    private final PedidoRepository pedidoRepository; // Inyectamos el repositorio de Pedidos

    public ReporteJPAAdapter(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<ReporteVentasTrabajador> generarReporteVentasPorTrabajador(LocalDate fechaInicio, LocalDate fechaFin) {
        List<PedidoEntity> pedidos;

        // Convertir LocalDate a LocalDateTime para las consultas
        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : null;
        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(LocalTime.MAX) : null; // Fin del día

        if (inicio != null && fin != null) {
            // Si hay rango de fechas, filtrar por fecha
            pedidos = pedidoRepository.findByFechaPedidoBetween(inicio, fin);
        } else {
            // Si no hay rango de fechas, obtener todos los pedidos
            pedidos = pedidoRepository.findAll();
        }

        // Agrupar pedidos por el email del trabajador y calcular sus ventas totales
        Map<String, List<PedidoEntity>> pedidosPorTrabajador = pedidos.stream()
                .collect(Collectors.groupingBy(PedidoEntity::getUsuarioEmail));

        List<ReporteVentasTrabajador> reporteFinal = new ArrayList<>();

        pedidosPorTrabajador.forEach((emailTrabajador, listaPedidosTrabajador) -> {
            double totalVentasTrabajador = 0;
            long totalPedidosTrabajador = 0; // Nuevo contador de pedidos por trabajador
            // Mapa para sumar la cantidad vendida de cada producto por este trabajador
            Map<String, Integer> productosVendidosPorTrabajador = new java.util.HashMap<>();

            for (PedidoEntity pedido : listaPedidosTrabajador) {
                totalPedidosTrabajador++; // Incrementar el contador de pedidos
                // Asegurarse de que los detalles del pedido estén cargados (gracias a @Transactional en CasoUso)
                if (pedido.getDetalles() != null) {
                    for (DetallePedidoEntity detalle : pedido.getDetalles()) {
                        double subtotalDetalle = detalle.getCantidad() * detalle.getPrecio();
                        totalVentasTrabajador += subtotalDetalle;

                        // Sumar la cantidad de cada producto vendido
                        productosVendidosPorTrabajador.merge(detalle.getSabor(), detalle.getCantidad(), Integer::sum);
                    }
                }
            }

            // Crear el objeto del modelo de dominio del reporte
            ReporteVentasTrabajador reporte = new ReporteVentasTrabajador(
                    emailTrabajador,
                    totalVentasTrabajador,
                    productosVendidosPorTrabajador,
                    totalPedidosTrabajador
            );
            reporteFinal.add(reporte);
        });

        return reporteFinal;
    }
}
