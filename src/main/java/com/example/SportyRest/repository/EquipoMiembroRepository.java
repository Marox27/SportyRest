package com.example.SportyRest.repository;

import com.example.SportyRest.model.Equipo_miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoMiembroRepository extends JpaRepository<Equipo_miembro, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
    List<Equipo_miembro> findByEquipoId(int equipoId);
    List<Equipo_miembro> findByUsuarioId(int usuarioId);
}

