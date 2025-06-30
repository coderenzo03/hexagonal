package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.puertos.salida.PedidoRepositorio;
import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PedidoRepositorioImpl implements PedidoRepositorio {

    private final PedidoJpaRepository jpaRepository;

    public PedidoRepositorioImpl(PedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setUsuarioEmail(pedido.getUsuarioEmail());
        entity.setFechaPedido(pedido.getFechaPedido());
        PedidoEntity saved = jpaRepository.save(entity);
        return toModel(saved);
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return jpaRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    private Pedido toModel(PedidoEntity entity) {
        Pedido pedido = new Pedido();
        pedido.setId(entity.getId());
        pedido.setUsuarioEmail(entity.getUsuarioEmail());
        pedido.setFechaPedido(entity.getFechaPedido());
        return pedido;
    }
}
