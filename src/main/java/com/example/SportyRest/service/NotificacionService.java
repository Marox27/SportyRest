package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.NotificacionRepository;
import com.example.SportyRest.repository.ParticipanteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ParticipanteRepository participanteRepository;

    public List<Notificacion> getNotificacionesByUsuarioNoLeidas(int idUsuario) {
        Usuario receptor = usuarioRepository.findByIdusuario(idUsuario);
        return notificacionRepository.findByReceptorAndLeidaIsFalse(receptor);
    }

    public List<Notificacion> getNotificacionesByUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findByIdusuario(idUsuario);
        return notificacionRepository.findByReceptor(usuario);
    }

    public Notificacion crearNotificacion(Notificacion notificacion) {
        notificacion.setFechaCreacion(LocalDateTime.now().toString());
        return notificacionRepository.save(notificacion);
    }

    public Boolean marcarNotificacionesComoLeidas(List<Integer> idsNotificaciones){
        if (idsNotificaciones == null || idsNotificaciones.isEmpty()) {
            return false;
        }

        for (int id : idsNotificaciones) {
            Notificacion notificacion = notificacionRepository.findById(id);
            if (notificacion != null) {
                notificacion.setLeida(true);
                notificacionRepository.save(notificacion);
            }
        }
        return true;
    }

    // Envia una notificación a todos los participantes cuando la actividad ha terminado
    public void enviarNotificacionesFinActividad(Actividad actividad) {
        List<Participante> participantes = participanteRepository.findByActividad(actividad);
        Usuario creador = usuarioRepository.findByIdusuario(actividad.getCreador());
        Usuario SportyHub = usuarioRepository.findByIdusuario(1);

        for (Participante participante : participantes) {
            Usuario usuario = participante.getUsuario();
            if (usuario.getId().intValue() != creador.getId().intValue()){
                Notificacion notificacion = new Notificacion();
                notificacion.setEmisor(creador); // El creador envía la notificación
                notificacion.setReceptor(usuario);
                notificacion.setTitulo("Actividad finalizada");
                notificacion.setMensaje("La actividad '" + actividad.getTitulo() + "' ha finalizado.");
                notificacionRepository.save(notificacion);
            }else{
                Notificacion notificacion = new Notificacion();
                notificacion.setEmisor(SportyHub); // El creador envía la notificación
                notificacion.setReceptor(usuario);
                notificacion.setTitulo("Actividad finalizada");
                notificacion.setMensaje("La actividad '" + actividad.getTitulo() + "' que creaste ha finalizado.");
                notificacionRepository.save(notificacion);
            }
        }
    }

    // Envía una notificación a todos los participantes de la actividad cuando esta se cancela.
    public void enviarNotificacionesActividadCancelada(Actividad actividad) {
        List<Participante> participantes = participanteRepository.findByActividad(actividad);
        Usuario creador = usuarioRepository.findByIdusuario(actividad.getCreador());

        for (Participante participante : participantes) {
            Usuario usuario = participante.getUsuario();
            if (usuario.getId().intValue() != creador.getId().intValue()) {
                Notificacion notificacion = new Notificacion();
                notificacion.setEmisor(creador); // El creador envía la notificación
                notificacion.setReceptor(usuario);
                notificacion.setTitulo("Actividad cancelada");
                String mensaje = actividad.getPrecio() == 0 ?
                        "El creador ha cancelado la actividad " + actividad.getTitulo() + "." :
                        "El creador ha cancelado la actividad " + actividad.getTitulo() + ". En breves recibirá el reembolso de su participación";
                notificacion.setMensaje(mensaje);
                notificacionRepository.save(notificacion);
            }
        }
    }

    public void enviarNotificacionesActividadCanceladaAutomatica(Actividad actividad) {
        List<Participante> participantes = participanteRepository.findByActividad(actividad);
        Usuario SportyHub = usuarioRepository.findByIdusuario(1);

        for (Participante participante : participantes) {
            Usuario usuario = participante.getUsuario();
            Notificacion notificacion = new Notificacion();
            notificacion.setEmisor(SportyHub); // El creador envía la notificación
            notificacion.setReceptor(usuario);
            notificacion.setTitulo("Actividad cancelada");
            String mensaje = actividad.getPrecio() == 0 ?
                    "Debido a la falta de participantes a la hora de inicio hemos cancelado la actividad '" + actividad.getTitulo() + "'." :
                    "Debido a la falta de participantes a la hora de inicio hemos cancelado la actividad '" + actividad.getTitulo() + "'. Los participantes recibirán su reembolso en breves";
            notificacion.setMensaje(mensaje);
            notificacionRepository.save(notificacion);
        }
    }

}

