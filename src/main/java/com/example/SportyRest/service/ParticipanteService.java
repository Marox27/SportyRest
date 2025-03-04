package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.repository.ParticipanteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PagoService pagoService;
    @Autowired
    private NotificacionService notificacionService;

    @Transactional
    public Participante createParticipante(Participante participante) {
        return participanteRepository.save(participante);
    }

    @Transactional
    public void deleteParticipante(int id) {
        Participante participante = participanteRepository.findById(id).get();

        // Envía una notificación al lider, de que se han unido a su actividad.
        Notificacion notificacion = new Notificacion();
        notificacion.setEmisor(participante.getUsuario());
        notificacion.setReceptor(usuarioRepository.findByIdusuario(participante.getActividad().getCreador()));
        notificacion.setTitulo("Abandono a actividad");
        notificacion.setMensaje("¡" + participante.getUsuario().getNickname() + " ha abandonado la actividad " + participante.getActividad().getTitulo() + "!");
        notificacionService.crearNotificacion(notificacion);

        participanteRepository.delete(participante);
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

    @Transactional
    public boolean unirseActividad(int idActividad, int usuarioId) {
        try {
            // Crear un nuevo participante directamente con los IDs
            Participante participante = new Participante();
            participante.setActividad(actividadRepository.findById(idActividad));
            participante.setUsuario(usuarioRepository.findByIdusuario(usuarioId));
            participante.setPagoConfirmado(true);

            // Guardar el nuevo participante utilizando el servicio correspondiente
            participanteRepository.save(participante);

            // Envía una notificación al lider, de que se han unido a su actividad.
            Notificacion notificacion = new Notificacion();
            notificacion.setEmisor(participante.getUsuario());
            notificacion.setReceptor(usuarioRepository.findByIdusuario(participante.getActividad().getCreador()));
            notificacion.setTitulo("Unión a actividad");
            notificacion.setMensaje("¡" + participante.getUsuario().getNickname() + " se ha unido a la actividad " + participante.getActividad().getTitulo() + "!");
            notificacionService.crearNotificacion(notificacion);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina la participacion de un usuario en una actividad y le reembolsa el dinero.
    @Transactional
    public boolean cancelarParticipacionSinReembolso(int idUsuario, int idActividad){
        Usuario usuarioParticipante = usuarioRepository.findByIdusuario(idUsuario);
        Actividad actividad = actividadRepository.findById(idActividad);
        Optional<Participante> participante =
                Optional.ofNullable(participanteRepository.findByActividadAndUsuario(actividad, usuarioParticipante));

        if (participante.isPresent()) {
            participanteRepository.delete(participante.get());
            return true;
        }
        return false;
    }

    // Elimina la participacion de un usuario en una actividad y le reembolsa el dinero.
    @Transactional
    public boolean cancelarParticipacionYReembolso(int idUsuario, int idActividad){
        Usuario usuarioParticipante = usuarioRepository.findByIdusuario(idUsuario);
        Actividad actividad = actividadRepository.findById(idActividad);

        Optional<Participante> participante =
                Optional.ofNullable(participanteRepository.findByActividadAndUsuario(actividad, usuarioParticipante));

        if (participante.isPresent()) {
            if (actividad.getPrecio() > 0){
                pagoService.reembolsarPagoCancelacionUsuario(actividad, usuarioParticipante);
            }
            participanteRepository.delete(participante.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean cancelarParticipacionesUsuarioBaneado(Usuario usuario){
        List<Participante> participacionesActivasDeUsuario = participanteRepository.findActiveParticipationsByUser(usuario);

        if (participacionesActivasDeUsuario != null && !participacionesActivasDeUsuario.isEmpty()){
            for (Participante participante: participacionesActivasDeUsuario) {
                participanteRepository.delete(participante);
            }
            return true;
        }
        return false;
    }

}
