package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    // Crear actividad
    public void crearActividad(Actividad actividad) {
        // Verifica que la actividad tenga un ID asignado después de guardarla
        actividad.setFecha_publicacion(LocalDate.now().toString()); // Establece la fecha de publicación
        actividad.setActivo(true); // Activa la actividad por defecto

        // Guardar la actividad
        actividadRepository.save(actividad);
    }


    // Eliminar actividad por ID
    public boolean eliminarActividad(int idActividad) {
        Optional<Actividad> actividad = Optional.ofNullable(actividadRepository.findById(idActividad));
        if (actividad.isPresent()) {
            actividadRepository.deleteById(idActividad);
            return true;
        }
        return false; // Actividad no encontrada
    }

    public List<Actividad> getTodasActividades() {
        return actividadRepository.findAll();
    }

    public List<Actividad> getActividadesCercanas(double latitud, double longitud, double distancia) {
        List<Actividad> actividadesCercanas = actividadRepository.findActividadesCercanas(latitud, longitud, distancia);
        System.out.println("Actividades Cercanas: " + actividadesCercanas.size());
        return actividadesCercanas;
    }

    public List<Actividad> getActividadesPorUsuario(Long idUsuario) {
        return actividadRepository.findByCreador(idUsuario);
    }


}

