package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Pago;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // buscar un usuario por su nombre de usuario:
    Pago findById(int id);

    List<Pago> findByUsuario(Usuario usuario);

    List<Pago> findByActividadAndLiberadoFalse(Actividad actividad);

    List<Pago> findByActividadAndReembolsadoFalse(Actividad actividad);

}