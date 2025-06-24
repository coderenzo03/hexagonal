package com.utp.hexagonal.Dominio.puertos.salida;

import com.utp.hexagonal.Dominio.modelo.Pedido;
import java.util.List;
import java.util.Optional; // Podrías necesitarlo para buscar por ID

public interface PedidoPersistencePort {
    Pedido save(Pedido pedido); // Usa 'save' para coherencia con repositorios
    Optional<Pedido> findById(Long id); // Si necesitas buscar un pedido por ID
    List<Pedido> findAll();
}