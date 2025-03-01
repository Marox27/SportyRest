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
    public List<Reporte> obtenerReportesUsuario(int idUsuario) {
        return reporteService.obtenerReportesUsuario(idUsuario);
    }

    @GetMapping("/usuario-reportante")
    public List<Reporte> obtenerReportesUsuarioReportante(@RequestParam int idUsuario) {
        return reporteService.obtenerReportesUsuarioReportante(idUsuario);
    }

    @GetMapping("/usuario-reportado")
    public List<Reporte> obtenerReportesUsuarioReportado(@RequestParam int idUsuario) {
        return reporteService.obtenerReportesUsuarioReportado(idUsuario);
    }

    @GetMapping("/pendientes")
    public List<Reporte> obtenerReportesPendientes() {
        return reporteService.obtenerReportesPendientes();
    }

    @GetMapping("/comprobar-existente")
    public ResponseEntity<Boolean> comprobarReporteExistente(@RequestParam int idUsuario,
                                                             @RequestParam int idActividad,
                                                             @RequestParam String entidad){
        boolean existe = reporteService.comprobarReporteExistente(idUsuario, idActividad, entidad);
        return ResponseEntity.ok(existe);
    }

    @PutMapping("/revisar")
    public ResponseEntity<Boolean> marcarComoRevisado(@RequestParam int idReporte) {
        reporteService.marcarComoRevisado(idReporte);
        return ResponseEntity.ok(true);
    }

}
