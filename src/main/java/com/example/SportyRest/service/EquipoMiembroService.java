package com.example.SportyRest.service;

import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.EquipoMiembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EquipoMiembroService {

    @Autowired
    private EquipoMiembroRepository equipoMiembroRepository;

    public Equipo_miembro crearMiembro(Equipo_miembro equipoMiembro) {
        return equipoMiembroRepository.save(equipoMiembro);
    }

    public List<Equipo_miembro> obtenerMiembrosPorEquipo(Equipo equipoId) {
        return equipoMiembroRepository.findByEquipo(equipoId);
    }

    public List<Equipo_miembro> obtenerMiembrosPorUsuario(Usuario usuario) {
        return equipoMiembroRepository.findByUsuario(usuario);
    }

    public List<Usuario> obtenerUsuariosporEquipo(int equipo){
        return equipoMiembroRepository.findUsuariosByEquipoId(equipo);
    }

    public List<Equipo> obtenerEquiposPorUsuario(int usuario){
        return equipoMiembroRepository.findEquiposByUsuarioId(usuario);
    }

    public Optional<Equipo_miembro> obtenerMiembroPorId(int id) {
        return equipoMiembroRepository.findById(id);
    }

    public void eliminarMiembro(int id) {
        equipoMiembroRepository.deleteById(id);
    }
}

