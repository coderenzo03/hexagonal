package com.utp.hexagonal.Dominio.puertos.salida;

import com.utp.hexagonal.Dominio.modelo.Pedido;

import java.util.List;

public interface PedidoRepositorio {
    Pedido guardar(Pedido pedido);
    List<Pedido> obtenerTodos();
}
