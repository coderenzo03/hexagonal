package com.utp.hexagonal.Aplicacion.casodeuso;

import com.utp.hexagonal.Dominio.modelo.ReporteVentasTrabajador;
import com.utp.hexagonal.Dominio.puertos.entrada.ReporteService; // Nuevo puerto de entrada
import com.utp.hexagonal.Dominio.puertos.salida.ReporteRepository; // Nuestro nuevo puerto de salida
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Caso de uso para la generación de reportes de ventas.
@Service
public class ReporteCasoUso implements ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteCasoUso(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasTrabajador> generarReporteVentasPorTrabajador(LocalDate fechaInicio, LocalDate fechaFin) {
        return reporteRepository.generarReporteVentasPorTrabajador(fechaInicio, fechaFin);
    }
}
