package com.example.SportyRest.controller;

import com.example.SportyRest.model.Deporte;
import com.example.SportyRest.service.DeporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deportes")
public class DeporteController {

    @Autowired
    private DeporteService deporteService;

    @GetMapping
    public ResponseEntity<List<Deporte>> listarTodos() {
        return ResponseEntity.ok(deporteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deporte> obtenerPorId(@PathVariable int id) {
        return deporteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Deporte> guardarDeporte(@RequestBody Deporte deporte) {
        return ResponseEntity.ok(deporteService.guardarDeporte(deporte));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deporte> actualizarDeporte(@PathVariable int id, @RequestBody Deporte deporteActualizado) {
        return deporteService.obtenerPorId(id)
                .map(deporteExistente -> {
                    deporteExistente.setNombre(deporteActualizado.getNombre());
                    deporteExistente.setNormativa(deporteActualizado.getNormativa());
                    deporteExistente.setMinJugadores(deporteActualizado.getMinJugadores());
                    deporteExistente.setMaxJugadores(deporteActualizado.getMaxJugadores());
                    deporteService.guardarDeporte(deporteExistente);
                    return ResponseEntity.ok(deporteExistente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDeporte(@PathVariable int id) {
        if (deporteService.obtenerPorId(id).isPresent()) {
            deporteService.eliminarDeporte(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

