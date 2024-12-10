package com.example.SportyRest.service;

import com.example.SportyRest.model.Deporte;
import com.example.SportyRest.repository.DeporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeporteService {

    @Autowired
    private DeporteRepository deporteRepository;

    public List<Deporte> listarTodos() {
        return deporteRepository.findAll();
    }

    public Optional<Deporte> obtenerPorId(int id) {
        return deporteRepository.findById(id);
    }

    public Deporte guardarDeporte(Deporte deporte) {
        return deporteRepository.save(deporte);
    }

    public void eliminarDeporte(int id) {
        deporteRepository.deleteById(id);
    }


}

