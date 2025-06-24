package com.utp.hexagonal.Infraestructura.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {
    private String usuarioEmail;
    private List<DetalleRequest> detalles;

    @Getter
    @Setter
    public static class DetalleRequest {
        private String sabor;
        private int cantidad;
        private double precio;
    }
}
