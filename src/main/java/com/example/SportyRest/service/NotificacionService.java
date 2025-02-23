package com.example.SportyRest.service;

import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.NotificacionRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

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
}

