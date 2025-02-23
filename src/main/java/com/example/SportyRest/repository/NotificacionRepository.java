package com.example.SportyRest.repository;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    // buscar un usuario por su nombre de usuario:
    Notificacion findById(int id);

    List<Notificacion> findByReceptor(Usuario receptor);

    List<Notificacion> findByReceptorAndLeidaIsFalse(Usuario receptor);


}