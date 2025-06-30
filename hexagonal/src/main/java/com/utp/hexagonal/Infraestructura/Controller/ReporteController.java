package com.utp.hexagonal.Infraestructura.Controller;

import com.utp.hexagonal.Dominio.modelo.ReporteVentasTrabajador;
import com.utp.hexagonal.Dominio.puertos.entrada.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

// Controlador REST para la generación de reportes.
@RestController
@RequestMapping("/api/reportes") // Nuevo path para los endpoints de reportes
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }
    @GetMapping("/ventas-trabajador")
    @PreAuthorize("hasRole('ADMIN')") // Solo el administrador puede acceder a este reporte
    public ResponseEntity<List<ReporteVentasTrabajador>> generarReporteVentasPorTrabajador(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<ReporteVentasTrabajador> reporte = reporteService.generarReporteVentasPorTrabajador(fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }
}
