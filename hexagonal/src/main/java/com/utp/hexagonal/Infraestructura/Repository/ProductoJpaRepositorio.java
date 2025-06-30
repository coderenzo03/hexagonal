package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Infraestructura.Entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoJpaRepositorio extends JpaRepository<ProductoEntity, Long> {
    long countByStockLessThan(int stock);
    List<ProductoEntity> findAll();
    Optional<ProductoEntity> findById(Long id); // Explícitamente definido para claridad
    Optional<ProductoEntity> findByNombreIgnoreCase(String nombre);
}
