package com.utp.hexagonal.Infraestructura.Persistencia;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.salida.ProductoRepositorio;
import com.utp.hexagonal.Infraestructura.Entity.ProductoEntity;
import com.utp.hexagonal.Infraestructura.Repository.ProductoJpaRepositorio; // Usando tu nombre de repositorio
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class ProductoJPAAdapter implements ProductoRepositorio {

    private final ProductoJpaRepositorio productoJpaRepositorio;

    public ProductoJPAAdapter(ProductoJpaRepositorio productoJpaRepositorio) {
        this.productoJpaRepositorio = productoJpaRepositorio;
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = ProductoEntity.fromModel(producto);
        ProductoEntity guardado = productoJpaRepositorio.save(entity);
        return guardado.toModel();
    }

    @Override
    public List<Producto> listar() { // Renombrado a listar() para coincidir con el puerto
        return productoJpaRepositorio.findAll()
                .stream()
                .map(ProductoEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) { // Método para obtener por ID
        return productoJpaRepositorio.findById(id).map(ProductoEntity::toModel);
    }

    @Override
    public Producto actualizar(Producto producto) {
        ProductoEntity existingProduct = productoJpaRepositorio.findById(producto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado para actualizar con ID: " + producto.getId()));
        existingProduct.setNombre(producto.getNombre());
        existingProduct.setDescripcion(producto.getDescripcion());
        existingProduct.setPrecio(producto.getPrecio());
        existingProduct.setStock(producto.getStock()); // Asegura que el stock se actualice

        ProductoEntity actualizado = productoJpaRepositorio.save(existingProduct);
        return actualizado.toModel();
    }

    @Override
    public void eliminar(Long id) {
        productoJpaRepositorio.deleteById(id);
    }

    @Override
    public long countAllProducts() {
        return productoJpaRepositorio.count();
    }

    @Override
    public long countProductsWithStockLessThan(int threshold) {
        return productoJpaRepositorio.countByStockLessThan(threshold);
    }
}
