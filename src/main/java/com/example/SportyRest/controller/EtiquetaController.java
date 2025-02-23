package com.example.SportyRest.controller;

import com.example.SportyRest.model.Etiqueta;
import com.example.SportyRest.repository.EtiquetaRepository;
import com.example.SportyRest.service.EtiquetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tags")
public class EtiquetaController {

    @Autowired
    private EtiquetaService etiquetaService;
    @Autowired
    private EtiquetaRepository etiquetaRepository;

    // Endpoint para comprobar una lista de etiquetas
    @PostMapping("/check-tags")
    public ResponseEntity<?> comprobarEtiquetas(@RequestBody List<String> etiquetas) {
        try {
            Set<Etiqueta> etiquetas_seleccionadas = new HashSet<>();

            for (String etiqueta: etiquetas) {
                System.out.println("Etiqueta recibida en el controlador: " + etiqueta);
                etiquetas_seleccionadas.add(etiquetaRepository.findByNombre(etiqueta));
            }

            return ResponseEntity.ok(etiquetas_seleccionadas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al comprobar las etiquetas enviadas.");
        }
    }

}




