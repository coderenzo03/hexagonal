package com.utp.hexagonal.Infraestructura.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRegistroRequest {
    private String email;
    private String contrasena;
    private String rol; // Aunque será fijo a "USER", lo dejamos para flexibilidad futura
}