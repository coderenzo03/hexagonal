package com.utp.hexagonal.Dominio.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

// Modelo de Dominio para el reporte de ventas por trabajador
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentasTrabajador {
    private String emailTrabajador;
    private double totalVentas;
    private Map<String, Integer> productosVendidos;
    private long totalPedidos;
}
