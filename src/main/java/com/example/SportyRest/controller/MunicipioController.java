package com.example.SportyRest.controller;

import com.example.SportyRest.model.Municipio;
import com.example.SportyRest.service.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @GetMapping
    public List<Municipio> obtenerPorProvincia(@RequestParam("provinciaId") int provinciaId) {
        return municipioService.obtenerPorProvincia(provinciaId);
    }
}

