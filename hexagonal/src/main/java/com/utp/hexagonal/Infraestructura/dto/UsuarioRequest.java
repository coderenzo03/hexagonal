package com.utp.hexagonal.Infraestructura.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {
    private String email;
    private String contrasena;
    private String rol; // "USER"
}