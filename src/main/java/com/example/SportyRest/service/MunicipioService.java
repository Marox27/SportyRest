package com.example.SportyRest.service;

import com.example.SportyRest.model.Municipio;
import com.example.SportyRest.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    public List<Municipio> obtenerPorProvincia(int provinciaId) {
        return municipioRepository.findByProvinciaId(provinciaId);
    }
}

