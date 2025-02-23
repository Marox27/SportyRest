package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.repository.ParticipanteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Participante createParticipante(Participante participante) {
        return participanteRepository.save(participante);
    }

    public void deleteParticipante(int id) {
        participanteRepository.deleteById(id);
    }

    public List<Participante> getParticipantesByActividad(int actividadId) {
        Actividad actividad;
        try{
            actividad = actividadRepository.findById(actividadId);
            if (actividad == null){
                throw new ChangeSetPersister.NotFoundException();
            }
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
        return participanteRepository.findByActividad(actividad);
    }

    public List<Participante> getParticipantesByUsuario(int usuarioId) {
        Usuario usuario;
        try{
            usuario = usuarioRepository.findByIdusuario(usuarioId);
            if (usuario == null){
                throw new ChangeSetPersister.NotFoundException();
            }
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

        return participanteRepository.findByUsuario(usuario);
    }

    public Participante getParticipanteById(int id) {
        return participanteRepository.findById(id).orElse(null);
    }

    public List<Actividad> getActividadesPorUsuario(int idUsuario) {
        return participanteRepository.findActividadesByUsuarioId(idUsuario);
    }

    public boolean unirseActividad(int idActividad, int usuarioId) {
        try {
            // Crear un nuevo participante directamente con los IDs
            Participante participante = new Participante();
            participante.setActividad(actividadRepository.findById(idActividad));
            participante.setUsuario(usuarioRepository.findByIdusuario(usuarioId));
            participante.setPagoConfirmado(true);

            // Guardar el nuevo participante utilizando el servicio correspondiente
            participanteRepository.save(participante);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
