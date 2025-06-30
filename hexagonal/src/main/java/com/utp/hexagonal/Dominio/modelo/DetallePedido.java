package com.utp.hexagonal.Dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Este es el modelo de dominio para los detalles de un pedido.
// Representa un ítem específico dentro de un pedido (ej. 5 unidades de "Chocolate").
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {
    private Long id;
    private Long idProducto;
    private String sabor;
    private int cantidad;
    private double precio;
}
