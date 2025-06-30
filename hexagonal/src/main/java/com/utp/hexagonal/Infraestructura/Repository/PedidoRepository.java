package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Asegúrate de tener esta importación si la usas en otros métodos

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    // Método que ya habías añadido para buscar pedidos en un rango de fechas
    List<PedidoEntity> findByFechaPedidoBetween(LocalDateTime startDate, LocalDateTime endDate);
    long countByFechaPedidoBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT SUM(dp.cantidad * dp.precio) FROM PedidoEntity p JOIN p.detalles dp WHERE p.fechaPedido BETWEEN :startOfDay AND :endOfDay")
    Double sumTotalByFechaPedidoBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
