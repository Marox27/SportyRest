package com.example.SportyRest.controller;

import com.example.SportyRest.model.Reporte;
import com.example.SportyRest.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @PostMapping("/crear")
    public ResponseEntity<Reporte> crearReporte(@RequestBody Reporte reporte) {
        Reporte nuevoReporte = reporteService.crearReporte(reporte);
        return ResponseEntity.ok(nuevoReporte);
    }

    @GetMapping
    public List<Reporte> obtenerTodosLosReportes() {
        return reporteService.obtenerAllReportes();
    }

    @GetMapping("/usuario")
    public List<Reporte> obtenerReportesUsuario(@RequestParam int idUsuario) {
        return reporteService.obtenerReportesUsuario(idUsuario);
    }

    @GetMapping("/pendientes")
    public List<Reporte> obtenerReportesPendientes() {
        return reporteService.obtenerReportesPendientes();
    }

    @PutMapping("/revisar")
    public ResponseEntity<Boolean> marcarComoRevisado(@RequestParam int idReporte) {
        reporteService.marcarComoRevisado(idReporte);
        return ResponseEntity.ok(true);
    }

}
