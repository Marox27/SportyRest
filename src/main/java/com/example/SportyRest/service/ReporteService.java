package com.example.SportyRest.service;

import com.example.SportyRest.model.Reporte;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ReporteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final int MAX_REPORTES_PARA_BAN = 3;

    public Reporte crearReporte(Reporte reporte) {
        Reporte nuevoReporte = reporteRepository.save(reporte);
        verificarBaneo(reporte.getUsuarioReportado());
        return nuevoReporte;
    }

    public List<Reporte> obtenerAllReportes() {
        return reporteRepository.findAll();
    }

    public List<Reporte> obtenerReportesUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findByIdusuario(idUsuario);
        return reporteRepository.findByUsuarioReportante(usuario);
    }
    public List<Reporte> obtenerReportesPendientes() {
        return reporteRepository.findByRevisadoFalse();
    }

    public void marcarComoRevisado(int id) {
        Reporte reporte = reporteRepository.findById(id).orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporte.setRevisado(true);
        reporteRepository.save(reporte);
    }

    private void verificarBaneo(Usuario usuario) {
        int cantidadReportes = reporteRepository.countByUsuarioReportado(usuario);
        if (cantidadReportes >= MAX_REPORTES_PARA_BAN) {
            usuario.setBaneado(true);
            usuarioService.actualizarUsuario(usuario);
        }
    }
}
