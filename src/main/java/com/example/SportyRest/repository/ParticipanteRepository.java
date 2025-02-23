package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {
    List<Participante> findByActividad(Actividad actividad);
    List<Participante> findByUsuario(Usuario usuario);

    // Devuelve las actividades en las que participa un usuario dado su id de usuario.
    @Query("SELECT p.actividad FROM Participante p WHERE p.usuario.id = :idUsuario AND p.confirmado = true")
    List<Actividad> findActividadesByUsuarioId(@Param("idUsuario") int idUsuario);

}

