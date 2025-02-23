package com.example.SportyRest.service;

import com.example.SportyRest.model.Etiqueta;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class EtiquetaService {

    private static final Map<String, Set<String>> etiquetasIncompatibles = Map.of(
            "Intenso", Set.of("Casual"),
            "Casual", Set.of("Intenso"),
            "Competitivo", Set.of("Recreativo"),
            "Recreativo", Set.of("Competitivo")
    );

    public boolean validarEtiquetas(Set<Etiqueta> etiquetasSeleccionadas) {
        for (Etiqueta etiqueta : etiquetasSeleccionadas) {
            Set<String> incompatibles = etiquetasIncompatibles.get(etiqueta.getNombre());
            if (incompatibles != null) {
                for (Etiqueta otraEtiqueta : etiquetasSeleccionadas) {
                    if (incompatibles.contains(otraEtiqueta.getNombre())) {
                        return false; // Se detectó una combinación inválida
                    }
                }
            }
        }
        return true;
    }
}
