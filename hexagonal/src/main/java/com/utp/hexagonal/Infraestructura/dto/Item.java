package com.utp.hexagonal.Infraestructura.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long idProducto;
    private String sabor;
    private int cantidad;
    private double precio;
}
