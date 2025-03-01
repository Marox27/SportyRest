package com.example.SportyRest.controller;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.service.ParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    @PostMapping("/create")
    public ResponseEntity<Participante> createParticipante(@RequestBody Participante participante) {
        Participante newParticipante = participanteService.createParticipante(participante);
        return ResponseEntity.ok(newParticipante);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteParticipante(@PathVariable int id) {
        participanteService.deleteParticipante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<Participante>> getParticipantesByActividad(@PathVariable int actividadId) {
        List<Participante> participantes = participanteService.getParticipantesByActividad(actividadId);
        System.out.println("Devolviendo lista de participantes de la actividad con id " + actividadId);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Participante>> getParticipantesByUsuario(@PathVariable int usuarioId) {
        List<Participante> participantes = participanteService.getParticipantesByUsuario(usuarioId);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participante> getParticipanteById(@PathVariable int id) {
        Participante participante = participanteService.getParticipanteById(id);
        return participante != null ? ResponseEntity.ok(participante) : ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{idUsuario}/actividades")
    public ResponseEntity<List<Actividad>> getActividadesPorUsuario(@PathVariable int idUsuario) {
        List<Actividad> actividades = participanteService.getActividadesPorUsuario(idUsuario);
        return ResponseEntity.ok(actividades);
    }

    @PostMapping("/unirse")
    public ResponseEntity<Boolean> unirseActividadGratis(
            @RequestParam int idActividad,
            @RequestParam int usuarioId) {
        boolean resultado = participanteService.unirseActividad(idActividad, usuarioId);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/cancelar")
    public ResponseEntity<Boolean> cancelarParticipacion(
            @RequestParam int idActividad,
            @RequestParam int usuarioId) {
        boolean resultado = participanteService.cancelarParticipacionSinReembolso(idActividad, usuarioId);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/cancelar-con-reembolso")
    public ResponseEntity<Boolean> cancelarParticipacionConReembolso(
            @RequestParam int idActividad,
            @RequestParam int usuarioId) {
        boolean resultado = participanteService.cancelarParticipacionYReembolso(idActividad, usuarioId);
        return ResponseEntity.ok(resultado);
    }



}
