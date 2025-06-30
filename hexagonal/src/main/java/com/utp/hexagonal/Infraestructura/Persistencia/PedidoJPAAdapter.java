package com.utp.hexagonal.Infraestructura.Persistencia;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.modelo.DetallePedido; // Necesario para mapeo
import com.utp.hexagonal.Dominio.puertos.salida.PedidoPersistencePort; // Puerto de salida del Dominio
import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity;
import com.utp.hexagonal.Infraestructura.Entity.DetallePedidoEntity;
import com.utp.hexagonal.Infraestructura.Repository.PedidoRepository; // Tu Spring Data JPA Repo para PedidoEntity
import com.utp.hexagonal.Infraestructura.Repository.DetallePedidoRepository; // Tu Spring Data JPA Repo para DetallePedidoEntity
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList; // Importar ArrayList
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PedidoJPAAdapter implements PedidoPersistencePort {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    public PedidoJPAAdapter(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Override
    public Pedido save(Pedido pedido) {
        // Mapear el Pedido del dominio a una PedidoEntity
        PedidoEntity pedidoEntity = new PedidoEntity();
        if (pedido.getId() != null) {
            pedidoEntity.setId(pedido.getId()); // Si ya tiene un ID, se asume una actualización
        }

        pedidoEntity.setUsuarioEmail(pedido.getUsuarioEmail());
        pedidoEntity.setFechaPedido(pedido.getFechaPedido() != null ? pedido.getFechaPedido() : LocalDateTime.now());
        PedidoEntity savedPedidoEntity = pedidoRepository.save(pedidoEntity);

        // Mapear y guardar los detalles del pedido
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            List<DetallePedidoEntity> detallesEntities = pedido.getDetalles().stream()
                    .map(d -> {
                        DetallePedidoEntity detalleEntity = new DetallePedidoEntity();
                        detalleEntity.setSabor(d.getSabor());
                        detalleEntity.setCantidad(d.getCantidad());
                        detalleEntity.setPrecio(d.getPrecio());
                        detalleEntity.setPedido(savedPedidoEntity); // Establecer la relación con el pedido guardado
                        return detalleEntity;
                    })
                    .collect(Collectors.toList());

            // Guardar todos los detalles del pedido en la base de datos
            detallePedidoRepository.saveAll(detallesEntities);
            // Establecer la lista de detalles mapeados en la entidad guardada para la respuesta
            savedPedidoEntity.setDetalles(detallesEntities);
        } else {
            // Si no hay detalles, inicializar la lista para evitar NullPointerException al mapear a dominio
            savedPedidoEntity.setDetalles(new ArrayList<>());
        }
        // Si el pedido original del dominio no tenía ID, asignarle el ID generado
        if (pedido.getId() == null) {
            pedido.setId(savedPedidoEntity.getId());
        }
        // Actualizar los detalles del Pedido de dominio con los detalles guardados si fuera necesario
        pedido.setDetalles(savedPedidoEntity.getDetalles().stream()
                .map(this::mapDetalleToDomain)
                .collect(Collectors.toList()));
        return pedido; // Devolver el objeto Pedido del dominio actualizado
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id).map(this::mapToDomain); // Mapear si se encuentra
    }


    @Override
    public long countPedidosByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return pedidoRepository.countByFechaPedidoBetween(startDate, endDate);
    }

    @Override
    public double sumTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Double total = pedidoRepository.sumTotalByFechaPedidoBetween(startDate, endDate);
        return total != null ? total : 0.0; // Devuelve 0.0 si no hay ventas en el rango
    }


    private Pedido mapToDomain(PedidoEntity entity) {
        Pedido pedido = new Pedido();
        pedido.setId(entity.getId());
        pedido.setUsuarioEmail(entity.getUsuarioEmail());
        pedido.setFechaPedido(entity.getFechaPedido());
        // Mapear la lista de DetallePedidoEntity a DetallePedido (Dominio)
        if (entity.getDetalles() != null) {
            List<DetallePedido> detalles = entity.getDetalles().stream()
                    .map(this::mapDetalleToDomain)
                    .collect(Collectors.toList());
            pedido.setDetalles(detalles);
        }
        return pedido;
    }

    private DetallePedido mapDetalleToDomain(DetallePedidoEntity entity) {
        DetallePedido detalle = new DetallePedido();
        detalle.setSabor(entity.getSabor());
        detalle.setCantidad(entity.getCantidad());
        detalle.setPrecio(entity.getPrecio());
        // No mapees el PedidoEntity completo para evitar recursión y LazyInitializationException aquí.
        return detalle;
    }
}
