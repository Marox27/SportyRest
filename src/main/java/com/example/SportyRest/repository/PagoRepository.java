package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Pago;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.PageFormat;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // buscar un usuario por su nombre de usuario:
    Pago findById(int id);

    List<Pago> findByUsuario(Usuario usuario);

    // Buscar un pago por actividad y usuario que no ha sido reembolsado
    Pago findByActividadAndUsuarioAndReembolsadoFalse(Actividad actividad, Usuario usuario);


    List<Pago> findByActividadAndLiberadoFalse(Actividad actividad);

    List<Pago> findByActividadAndReembolsadoFalse(Actividad actividad);

}