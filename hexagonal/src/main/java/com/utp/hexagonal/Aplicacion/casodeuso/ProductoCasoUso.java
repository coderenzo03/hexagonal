package com.utp.hexagonal.Aplicacion.casodeuso;

import com.utp.hexagonal.Dominio.modelo.Producto;
import com.utp.hexagonal.Dominio.puertos.entrada.ProductoService;
import com.utp.hexagonal.Dominio.puertos.salida.ProductoRepositorio;

import java.util.List;
import java.util.Optional;

public class ProductoCasoUso implements ProductoService {

    private final ProductoRepositorio ProductoRepositorio;

    public ProductoCasoUso(ProductoRepositorio productoRepositorio) {
        this.ProductoRepositorio = productoRepositorio;
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return ProductoRepositorio.guardar(producto);
    }

    @Override
    public List<Producto> listarProductos() {
        return ProductoRepositorio.listar();
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        Optional<Producto> productoOpt = ProductoRepositorio.obtenerPorId(id);
        return productoOpt.orElse(null);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        producto.setId(id);
        return ProductoRepositorio.actualizar(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        ProductoRepositorio.eliminar(id);
    }
}
