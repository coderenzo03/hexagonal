package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.puertos.entrada.PedidoService; // ¡El PedidoService del dominio!
import com.utp.hexagonal.Infraestructura.dto.PedidoRequest; // Necesitarás el DTO aquí
import com.utp.hexagonal.Dominio.modelo.DetallePedido; // Necesitarás el modelo de DetallePedido para mapear
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime; // Necesario si asignas fecha aquí
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Para mapear detalles

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService; // Esto inyectará PedidoCasoUso

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crear") // Cambia el endpoint para evitar confusiones con guardarPedido
    public ResponseEntity<Pedido> crearPedido(@RequestBody PedidoRequest request) {
        // Mapear PedidoRequest (DTO de Infraestructura) a Pedido (Modelo de Dominio)
        Pedido pedidoDominio = new Pedido();
        // Asumo que tu Pedido del dominio ahora tiene setters para estas propiedades
        pedidoDominio.setUsuarioEmail(request.getUsuarioEmail());
        pedidoDominio.setFechaPedido(LocalDateTime.now()); // O de donde venga la fecha

        List<DetallePedido> detallesDominio = request.getDetalles().stream()
                .map(dr -> {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setSabor(dr.getSabor());
                    detalle.setCantidad(dr.getCantidad());
                    detalle.setPrecio(dr.getPrecio());
                    // No necesitas setear el Pedido aquí, es parte de la colección
                    return detalle;
                }).collect(Collectors.toList());

        pedidoDominio.setDetalles(detallesDominio);

        // Llamar al puerto de entrada del dominio (implementado por PedidoCasoUso)
        Pedido nuevoPedidoDominio = pedidoService.guardarPedido(pedidoDominio);

        // Opcional: Mapear el Pedido (dominio) de vuelta a un PedidoResponse (DTO) si lo necesitas
        return ResponseEntity.ok(nuevoPedidoDominio); // Devolver el modelo de dominio directamente o mapeado a un DTO de respuesta
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }
}