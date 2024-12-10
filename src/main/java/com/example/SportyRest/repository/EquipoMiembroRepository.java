package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoMiembroRepository extends JpaRepository<Equipo_miembro, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
    List<Equipo_miembro> findByEquipo(Equipo equipoId);
    List<Equipo_miembro> findByUsuario(Usuario usuarioId);

    // Devuelve los usuarios que pertenecen a un equipo dado su id de equipo.
    @Query("SELECT e.usuario FROM Equipo_miembro e WHERE e.equipo.id = :idEquipo")
    List<Usuario> findUsuariosByEquipoId(@Param("idEquipo") int idEquipo);

    // Devuelve los equipos a lo que pertenece un usuario dado su id de usuario.
    @Query("SELECT e.equipo FROM Equipo_miembro e WHERE e.usuario.id = :idUsuario")
    List<Equipo> findEquiposByUsuarioId(@Param("idUsuario") int idUsuario);

}

