package com.utp.hexagonal.Dominio.puertos.entrada;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import java.util.List;
import java.util.Optional;

// Esta interfaz define el puerto de entrada para las operaciones de Pedido en el Dominio.
// Es lo que la capa de Infraestructura (e.g., PedidoController) consume.
public interface PedidoService {
    Pedido guardarPedido(Pedido pedido);
    List<Pedido> listarPedidos();
    Optional<Pedido> obtenerPedidoPorId(Long id);
    List<Pedido> obtenerTodosLosPedidos(); // Nuevo método para el historial
    long countPedidosToday(); // Agregado para KPI
    double sumVentasToday(); // Agregado para KPI
}
