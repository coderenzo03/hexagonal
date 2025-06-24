package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Infraestructura.Entity.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long> {
}
