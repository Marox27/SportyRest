package com.example.SportyRest.service;

import com.example.SportyRest.model.Provincia;
import com.example.SportyRest.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public List<Provincia> obtenerTodas() {
        return provinciaRepository.findAll();
    }
}

