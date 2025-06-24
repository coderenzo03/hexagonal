package com.utp.hexagonal.Dominio.puertos.salida;

import com.utp.hexagonal.Dominio.modelo.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositorio {
    Producto guardar(Producto producto);
    List<Producto> listarTodos();

    Optional<Producto> obtenerPorId(Long id);
    List<Producto> listar();

    Optional<Producto> buscarPorId(Long id);
    Producto actualizar(Producto producto);
    void eliminar(Long id);
}
