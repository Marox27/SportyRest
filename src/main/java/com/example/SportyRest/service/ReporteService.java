package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Reporte;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.repository.EquipoRepository;
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
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private EquipoRepository equipoRepository;

    private static final int MAX_REPORTES_PARA_BAN = 3;

    public Reporte crearReporte(Reporte reporte) {
        Reporte nuevoReporte = reporteRepository.save(reporte);
        //verificarBaneo(reporte.getUsuarioReportado());
        return nuevoReporte;
    }

    public List<Reporte> obtenerAllReportes() {
        return reporteRepository.findAll();
    }

    public List<Reporte> obtenerReportesUsuario(int idUsuario) {
        return reporteRepository.obtenerReportesUsuario(idUsuario);
    }

    public List<Reporte> obtenerReportesUsuarioReportante(int idUsuario) {
        Usuario usuario = usuarioRepository.findByIdusuario(idUsuario);
        return reporteRepository.findByUsuarioReportante(usuario);
    }

    public List<Reporte> obtenerReportesUsuarioReportado(int idUsuario) {
        Usuario usuario = usuarioRepository.findByIdusuario(idUsuario);
        return reporteRepository.findByUsuarioReportado(usuario);
    }

    public List<Reporte> obtenerReportesPendientes() {
        return reporteRepository.findByRevisadoFalse();
    }

    public Boolean comprobarReporteExistente(int idUsuario, int idEntidad, String entidad){
        Usuario usuarioReportante = usuarioRepository.findByIdusuario(idUsuario);
        Reporte reporte = null;
        switch (entidad){
            case "ACTIVIDAD":
                Actividad actividadReportada = actividadRepository.findById(idEntidad);
                reporte = reporteRepository.findByUsuarioReportanteAndActividadReportada(usuarioReportante, actividadReportada);
                break;

            case "USUARIO":
                Usuario usuarioReportado = usuarioRepository.findByIdusuario(idEntidad);
                reporte = reporteRepository.findByUsuarioReportanteAndUsuarioReportado(usuarioReportante, usuarioReportado);
                break;

            case "EQUIPO":
                Equipo equipoReportado = equipoRepository.findById(idEntidad).get();
                reporte = reporteRepository.findByUsuarioReportanteAndEquipoReportado(usuarioReportante, equipoReportado);
                break;
        }

        return reporte != null;
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
