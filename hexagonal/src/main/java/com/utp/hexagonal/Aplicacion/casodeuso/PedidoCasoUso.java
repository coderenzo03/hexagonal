package com.utp.hexagonal.Aplicacion.casodeuso;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import com.utp.hexagonal.Dominio.puertos.entrada.PedidoService; // Tu puerto de entrada
import com.utp.hexagonal.Dominio.puertos.salida.PedidoPersistencePort; // ¡Nuevo puerto de salida!
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Este sigue siendo el bean que tu controlador inyectará
public class PedidoCasoUso implements PedidoService { // Implementa el puerto de entrada

    // Ahora inyecta el puerto de salida
    private final PedidoPersistencePort pedidoPersistencePort;

    public PedidoCasoUso(PedidoPersistencePort pedidoPersistencePort) {
        this.pedidoPersistencePort = pedidoPersistencePort;
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        // Aquí puedes añadir cualquier lógica de negocio antes de guardar
        // Por ejemplo, validaciones, cálculos, etc.
        return pedidoPersistencePort.save(pedido); // Llama al puerto de salida
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoPersistencePort.findAll(); // Llama al puerto de salida
    }
}