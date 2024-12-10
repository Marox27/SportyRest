package com.example.SportyRest.repository;

import com.example.SportyRest.model.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    List<Municipio> findByProvinciaId(int provinciaId);
}
