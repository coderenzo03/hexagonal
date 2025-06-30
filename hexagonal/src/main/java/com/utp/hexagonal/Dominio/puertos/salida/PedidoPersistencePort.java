package com.utp.hexagonal.Dominio.puertos.salida;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import java.time.LocalDateTime; // Importación necesaria
import java.util.List;
import java.util.Optional;


public interface PedidoPersistencePort {
    Pedido save(Pedido pedido); // Guarda o actualiza un pedido
    Optional<Pedido> findById(Long id); // Busca un pedido por ID
    List<Pedido> findAll(); // Obtiene todos los pedidos (ya existe y es útil para el historial)
    long countPedidosByDateRange(LocalDateTime startDate, LocalDateTime endDate); // Nuevo método para KPI
    double sumTotalSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate); // Nuevo método para KPI
}
