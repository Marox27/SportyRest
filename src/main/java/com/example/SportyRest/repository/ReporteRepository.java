package com.example.SportyRest.repository;

import com.example.SportyRest.model.Reporte;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByUsuarioReportante(Usuario usuario);
    List<Reporte> findByRevisadoFalse();
    int countByUsuarioReportado(Usuario usuario);

}
