package com.example.SportyRest.service;

import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.repository.EquipoMiembroRepository;
import com.example.SportyRest.repository.EquipoRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private  EquipoMiembroRepository equipoMiembroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear un equipo
    @Transactional
    public Equipo createEquipo(Equipo equipo) {
        // Guardar el equipo
        Equipo equipoGuardado = equipoRepository.save(equipo);

        // Crear el miembro (creador del equipo)
        Equipo_miembro equipoMiembro = new Equipo_miembro();
        equipoMiembro.setEquipo(equipoGuardado);
        equipoMiembro.setUsuario(usuarioRepository.findByIdusuario(equipoGuardado.getCreador()));
        equipoMiembro.setRol(Equipo_miembro.Rol.ADMIN);

        // Guardar el equipo miembro
        equipoMiembroRepository.save(equipoMiembro);

        return equipoGuardado;
    }


    // Obtener todos los equipos
    public List<Equipo> getAllEquipos() {
        return equipoRepository.findAll();
    }

    // Obtener un equipo por ID
    public Optional<Equipo> getEquipoById(int id) {
        return equipoRepository.findById(id);
    }

    // Actualizar un equipo
    public Equipo updateEquipo(int id, Equipo equipoDetails) {
        return equipoRepository.findById(id).map(equipo -> {
            equipo.setNombre(equipoDetails.getNombre());
            equipo.setProvincia(equipoDetails.getProvincia());
            equipo.setMunicipio(equipoDetails.getMunicipio());
            equipo.setPrivacidad(equipoDetails.getPrivacidad());
            equipo.setDetalles(equipoDetails.getDetalles());
            equipo.setImagen(equipoDetails.getImagen());
            equipo.setMiembros(equipoDetails.getMiembros());
            equipo.setCreador(equipoDetails.getCreador());
            equipo.setDeporte(equipoDetails.getDeporte());
            return equipoRepository.save(equipo);
        }).orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " + id));
    }

    // Eliminar un equipo
    public void deleteEquipo(int id) {
        equipoRepository.deleteById(id);
    }


}

