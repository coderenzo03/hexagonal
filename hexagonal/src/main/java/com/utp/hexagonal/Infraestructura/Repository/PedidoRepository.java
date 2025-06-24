package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
}
