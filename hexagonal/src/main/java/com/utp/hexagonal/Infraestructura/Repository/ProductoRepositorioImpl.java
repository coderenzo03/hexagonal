package com.utp.hexagonal.Infraestructura.Repository;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.salida.ProductoRepositorio;
import com.utp.hexagonal.Infraestructura.Entity.ProductoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductoRepositorioImpl implements ProductoRepositorio {

    private final JpaProductoRepository jpaProductoRepository;

    @Autowired
    public ProductoRepositorioImpl(JpaProductoRepository jpaProductoRepository) {
        this.jpaProductoRepository = jpaProductoRepository;
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = ProductoEntity.fromModel(producto);
        ProductoEntity saved = jpaProductoRepository.save(entity);
        return saved.toModel();
    }

    @Override
    public List<Producto> listar() {
        return jpaProductoRepository.findAll()
                .stream()
                .map(ProductoEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return jpaProductoRepository.findById(id).map(ProductoEntity::toModel);
    }

    @Override
    public Producto actualizar(Producto producto) {
        ProductoEntity entity = ProductoEntity.fromModel(producto);
        ProductoEntity updated = jpaProductoRepository.save(entity);
        return updated.toModel();
    }

    @Override
    public List<Producto> listarTodos() {
        return listar(); // reutilizas listar()
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return buscarPorId(id); // reutilizas buscarPorId()
    }

    @Override
    public void eliminar(Long id) {
        jpaProductoRepository.deleteById(id);
    }
}
