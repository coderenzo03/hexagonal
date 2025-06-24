package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Infraestructura.Entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductoRepository extends JpaRepository<ProductoEntity, Long> {
    // Métodos personalizados si deseas (opcional)
}
