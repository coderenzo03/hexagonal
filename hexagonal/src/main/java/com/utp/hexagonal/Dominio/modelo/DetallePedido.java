package com.utp.hexagonal.Dominio.modelo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DetallePedido {

    private String sabor;
    private int cantidad;
    private double precio;
}
