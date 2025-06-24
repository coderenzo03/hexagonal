package com.utp.hexagonal.Infraestructura.Persistencia;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.modelo.DetallePedido; // Necesario para mapeo
import com.utp.hexagonal.Dominio.puertos.salida.PedidoPersistencePort; // <<-- ¡Importante!
import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity;
import com.utp.hexagonal.Infraestructura.Entity.DetallePedidoEntity;
import com.utp.hexagonal.Infraestructura.Repository.PedidoRepository; // Tu Spring Data JPA Repo
import com.utp.hexagonal.Infraestructura.Repository.DetallePedidoRepository; // Tu Spring Data JPA Repo
import org.springframework.stereotype.Repository; // <<-- ¡Esta anotación!

import java.time.LocalDateTime;
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
    public Pedido save(Pedido pedido) { // Renombrado de guardarPedido a save, como habíamos discutido
        PedidoEntity pedidoEntity = new PedidoEntity();
        if (pedido.getId() != null) { // Para manejar actualizaciones
            pedidoEntity.setId(pedido.getId());
        }
        pedidoEntity.setSabor(pedido.getSabor()); // Si tu Pedido de dominio tiene sabor
        pedidoEntity.setCantidad(pedido.getCantidad()); // Si tu Pedido de dominio tiene cantidad
        pedidoEntity.setUsuarioEmail(pedido.getUsuarioEmail());
        pedidoEntity.setFechaPedido(pedido.getFechaPedido() != null ? pedido.getFechaPedido() : LocalDateTime.now()); // Usa la del dominio o genera una

        // Guarda el PedidoEntity principal para obtener su ID antes de guardar los detalles
        pedidoEntity = pedidoRepository.save(pedidoEntity);

        // Mapeo y guardado de detalles
        PedidoEntity finalPedidoEntity = pedidoEntity; // Necesario para el lambda
        List<DetallePedidoEntity> detallesEntities = pedido.getDetalles().stream()
                .map(d -> {
                    DetallePedidoEntity detalle = new DetallePedidoEntity();
                    detalle.setSabor(d.getSabor());
                    detalle.setCantidad(d.getCantidad());
                    detalle.setPrecio(d.getPrecio());
                    detalle.setPedido(finalPedidoEntity); // Establecer la relación
                    return detalle;
                })
                .collect(Collectors.toList());

        detallePedidoRepository.saveAll(detallesEntities);

        // Mapear de vuelta a objeto de dominio para devolver (actualizando el ID generado)
        if (pedido.getId() == null) {
            pedido.setId(finalPedidoEntity.getId());
        }
        return pedido;
    }

    @Override
    public List<Pedido> findAll() {
        return pedidoRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id).map(this::mapToDomain);
    }

    // Métodos de mapeo auxiliares (pueden estar en un mapper separado si quieres)
    private Pedido mapToDomain(PedidoEntity entity) {
        Pedido pedido = new Pedido();
        pedido.setId(entity.getId());
        pedido.setSabor(entity.getSabor());
        pedido.setCantidad(entity.getCantidad());
        pedido.setUsuarioEmail(entity.getUsuarioEmail());
        pedido.setFechaPedido(entity.getFechaPedido());
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
        // No mapees el PedidoEntity completo para evitar recursión
        return detalle;
    }
}