package com.utp.hexagonal.Aplicacion.casodeuso;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.entrada.ProductoService;
import com.utp.hexagonal.Dominio.puertos.salida.ProductoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación necesaria para @Transactional

import java.util.List;

@Service
public class ProductoCasoUso implements ProductoService {

    private final ProductoRepositorio productoRepositorio;

    public ProductoCasoUso(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepositorio.guardar(producto);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepositorio.listar();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepositorio.obtenerPorId(id).orElse(null);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        producto.setId(id);
        return productoRepositorio.actualizar(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepositorio.eliminar(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllProducts() {
        return productoRepositorio.countAllProducts();
    }

    @Override
    @Transactional(readOnly = true)
    public long countProductsWithStockLessThan(int threshold) {
        return productoRepositorio.countProductsWithStockLessThan(threshold);
    }
}
