package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Infraestructura.Entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {
}
