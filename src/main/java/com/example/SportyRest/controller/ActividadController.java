package com.example.SportyRest.controller;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.repository.ParticipanteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import com.example.SportyRest.service.ActividadService;
import com.example.SportyRest.service.ParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para crear una actividad
    @PostMapping("/crear")
    public ResponseEntity<?> crearActividad(@RequestBody Actividad actividad) {
        try {
            System.out.println("Fecha recibida en el controlador: " + actividad.getFecha());

            // Primero guarda la actividad
            actividadService.crearActividad(actividad);  // Aquí se guarda la actividad

            Actividad actividadGuardada = actividadRepository.findById(actividad.getIdactividad());

            // Crear un participante para esta actividad
            Participante participante = new Participante();
            participante.setActividad(actividadGuardada);  // Usa la actividad guardada
            participante.setUsuario(usuarioRepository.findById(actividad.getCreador()));
            participante.setFechaIngreso(LocalDateTime.now().toString());
            participante.setActivo(true);  // El participante está activo

            // Guardar el participante
            participanteRepository.save(participante);

            return ResponseEntity.ok("Actividad creada y participante añadido");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al crear la actividad o el participante");
        }
    }


    // Endpoint para eliminar una actividad por ID
    @DeleteMapping("/eliminar/{idActividad}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable int idActividad) {
        boolean eliminado = actividadService.eliminarActividad(idActividad);
        if (eliminado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Actividad>> getActividades() {
        List<Actividad> actividades = actividadService.getTodasActividades();
        return ResponseEntity.ok(actividades);
    }

    @GetMapping("/cercanas")
    public ResponseEntity<List<Actividad>> getActividadesCercanas(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam double distancia) {
        List<Actividad> actividadesCercanas = actividadService.getActividadesCercanas(latitud, longitud, distancia);
        return ResponseEntity.ok(actividadesCercanas);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Actividad>> getActividadesUsuario(@PathVariable int idUsuario) {
        List<Actividad> actividadesUsuario = actividadService.getActividadesPorUsuario(idUsuario);
        return ResponseEntity.ok(actividadesUsuario);
    }


}
