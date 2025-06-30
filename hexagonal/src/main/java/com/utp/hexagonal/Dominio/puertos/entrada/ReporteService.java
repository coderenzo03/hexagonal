package com.utp.hexagonal.Dominio.puertos.entrada;

import com.utp.hexagonal.Dominio.modelo.ReporteVentasTrabajador;
import java.time.LocalDate;
import java.util.List;

// Puerto de entrada para los casos de uso de reportes.
// Define las operaciones que la capa de Infraestructura puede solicitar relacionadas con reportes.
public interface ReporteService {
    List<ReporteVentasTrabajador> generarReporteVentasPorTrabajador(LocalDate fechaInicio, LocalDate fechaFin);
}
