package com.utp.hexagonal.Dominio.puertos.salida;

import com.utp.hexagonal.Dominio.modelo.Producto;
import java.util.List;
import java.util.Optional;

// Esta interfaz define el puerto de salida para las operaciones de persistencia de Producto
// que el dominio necesita.
public interface ProductoRepositorio {
    Producto guardar(Producto producto);
    List<Producto> listar(); // Usar listar() en lugar de listarTodos() para consistencia con tu uso
    Optional<Producto> obtenerPorId(Long id); // Asegurado que este método existe
    Producto actualizar(Producto producto);
    void eliminar(Long id);
    long countAllProducts(); // Para KPI de admin
    long countProductsWithStockLessThan(int threshold); // Para KPI de admin
}
