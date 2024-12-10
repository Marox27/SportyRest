package com.example.SportyRest.controller;

import com.example.SportyRest.model.Provincia;
import com.example.SportyRest.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/provincias")
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping
    public List<Provincia> obtenerTodas() {
        return provinciaService.obtenerTodas();
    }
}

