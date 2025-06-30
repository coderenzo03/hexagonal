package com.utp.hexagonal.Dominio.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private Long id;
    private String usuarioEmail;
    private LocalDateTime fechaPedido;
    private List<DetallePedido> detalles;

}
