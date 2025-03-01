package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Reporte;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByUsuarioReportante(Usuario usuario);
    List<Reporte> findByUsuarioReportado(Usuario usuario);
    @Query("SELECT r FROM Reporte r WHERE r.usuarioReportado.id = :idUsuario OR r.usuarioReportante.id = :idUsuario")
    List<Reporte> obtenerReportesUsuario(@Param("idUsuario") int idUsuario);
    List<Reporte> findByRevisadoFalse();

    Reporte findByUsuarioReportanteAndUsuarioReportado(Usuario usuarioReportante, Usuario usuarioReportado);
    Reporte findByUsuarioReportanteAndActividadReportada(Usuario usuario, Actividad actividad);
    Reporte findByUsuarioReportanteAndEquipoReportado(Usuario usuario, Equipo equipo);
    int countByUsuarioReportado(Usuario usuario);



}
