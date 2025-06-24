package com.utp.hexagonal.Dominio.puertos.entrada;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import java.util.List;

public interface PedidoService {
    Pedido guardarPedido(Pedido pedido);
    List<Pedido> listarPedidos();
}
