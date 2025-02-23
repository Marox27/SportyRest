package com.example.SportyRest.repository;

import com.example.SportyRest.model.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, Integer> {
    Etiqueta findByNombre(String nombre);

    Etiqueta findById(int id);
}
