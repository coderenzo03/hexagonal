package com.utp.hexagonal.Infraestructura.Persistencia;

import com.utp.hexagonal.Infraestructura.Entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoJpaRepositorio extends JpaRepository<ProductoEntity, Long> {
}
