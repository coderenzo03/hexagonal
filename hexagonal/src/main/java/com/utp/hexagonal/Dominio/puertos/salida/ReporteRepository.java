package com.utp.hexagonal.Dominio.puertos.salida;

import com.utp.hexagonal.Dominio.modelo.ReporteVentasTrabajador;
import java.time.LocalDate;
import java.util.List;


public interface ReporteRepository {
    List<ReporteVentasTrabajador> generarReporteVentasPorTrabajador(LocalDate fechaInicio, LocalDate fechaFin);
}
