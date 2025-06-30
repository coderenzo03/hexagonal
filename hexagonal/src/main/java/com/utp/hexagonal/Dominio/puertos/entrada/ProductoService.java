package com.utp.hexagonal.Dominio.puertos.entrada;

import com.utp.hexagonal.Dominio.modelo.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface ProductoService {
    Producto guardarProducto(Producto producto);
    List<Producto> listarProductos();
    Producto obtenerProductoPorId(Long id);
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
    long countAllProducts();
    long countProductsWithStockLessThan(int threshold);
}
