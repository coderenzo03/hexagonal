package com.utp.hexagonal.Infraestructura.Persistencia;

import com.utp.hexagonal.Dominio.puertos.entrada.ProductoService;
import com.utp.hexagonal.Dominio.puertos.salida.ProductoRepositorio;
import com.utp.hexagonal.Dominio.modelo.Producto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoJPAAdapter implements ProductoService {

    private final ProductoRepositorio productoRepository;

    public ProductoJPAAdapter(ProductoRepositorio productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.guardar(producto);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.listar(); // usa un solo método claro
    }

    @Override
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        // Validación opcional: verificar que exista antes de actualizar
        producto.setId(id);
        return productoRepository.actualizar(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.eliminar(id);
    }
}
