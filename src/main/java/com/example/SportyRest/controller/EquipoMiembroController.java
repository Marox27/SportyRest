package com.example.SportyRest.controller;

import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.service.EquipoMiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/equipo-miembro")
public class EquipoMiembroController {

    @Autowired
    private EquipoMiembroService equipoMiembroService;

    // Crear un miembro en un equipo
    @PostMapping
    public ResponseEntity<Equipo_miembro> crearMiembro(@RequestBody Equipo_miembro equipoMiembro) {
        Equipo_miembro nuevoMiembro = equipoMiembroService.crearMiembro(equipoMiembro);
        return new ResponseEntity<>(nuevoMiembro, HttpStatus.CREATED);
    }

    // Obtener todos los miembros de un equipo
    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<Equipo_miembro>> obtenerMiembrosPorEquipo(@PathVariable int equipoId) {
        List<Equipo_miembro> miembros = equipoMiembroService.obtenerMiembrosPorEquipo(equipoId);
        return new ResponseEntity<>(miembros, HttpStatus.OK);
    }

    // Obtener todos los equipos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Equipo_miembro>> obtenerMiembrosPorUsuario(@PathVariable int usuarioId) {
        List<Equipo_miembro> miembros = equipoMiembroService.obtenerMiembrosPorUsuario(usuarioId);
        return new ResponseEntity<>(miembros, HttpStatus.OK);
    }

    // Obtener un miembro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Equipo_miembro> obtenerMiembroPorId(@PathVariable int id) {
        Optional<Equipo_miembro> miembro = equipoMiembroService.obtenerMiembroPorId(id);
        return miembro.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Eliminar un miembro de un equipo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMiembro(@PathVariable int id) {
        equipoMiembroService.eliminarMiembro(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

