package com.example.SportyRest.service;

import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.EquipoMiembroRepository;
import com.example.SportyRest.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EquipoMiembroService {

    @Autowired
    private EquipoMiembroRepository equipoMiembroRepository;
    @Autowired
    private EquipoRepository equipoRepository;

    public Equipo_miembro crearMiembro(Equipo_miembro equipoMiembro) {
        // Guardamos el nuevo miembro
        Equipo_miembro equipo_miembro_guardado = equipoMiembroRepository.save(equipoMiembro);

        //Sí el guardado es exitoso actualizamos el número de miembros del equipo asociado
        Equipo equipo = equipoMiembro.getEquipo();
        equipo.setMiembros(equipo.getMiembros() + 1);
        equipoRepository.saveAndFlush(equipo);

        // Por último devolvemos los datos del miembro
        return equipo_miembro_guardado;
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

