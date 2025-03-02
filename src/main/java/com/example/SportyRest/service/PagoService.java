package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Pago;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.repository.NotificacionRepository;
import com.example.SportyRest.repository.PagoRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private NotificacionRepository notificacionRepository;

    @Transactional
    public Pago registrarPago(Pago pago) {
        pago.setReembolsado(false);
        pago.setLiberado(false); // Se marca como retenido hasta que la actividad finalice
        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagosDeUsuario(int idUsuario){
        Usuario usuario = usuarioRepository.findByIdusuario(idUsuario);
        return pagoRepository.findByUsuario(usuario);
    }

    // Se encargar de liberar los pagos que estaban retenidos de una actividad ya completada con éxito.
    @Transactional
    public void liberarPagos(Actividad actividad) {
        List<Pago> pagosPendientes = pagoRepository.findByActividadAndLiberadoFalse(actividad);

        if (!pagosPendientes.isEmpty()) {
            double total = pagosPendientes.stream().mapToDouble(Pago::getCantidad).sum();
            Usuario creador = usuarioRepository.findByIdusuario(actividad.getCreador());


            // Simulación de transferencia de dinero al creador y su respectiva notificación.
            System.out.println("Transferidos " + total + "€ al creador: " + creador.getNickname());
            Notificacion notificacion = new Notificacion();
            notificacion.setTitulo("Actividad Finalizada");
            notificacion.setMensaje("La actividad ha finalizado y con ello recibiras el pago de los participantes en breve.");
            notificacion.setEmisor(usuarioRepository.findByIdusuario(1));
            notificacion.setReceptor(usuarioRepository.findByIdusuario(actividad.getCreador()));
            notificacion.setFechaCreacion(LocalDateTime.now().toString());
            notificacionRepository.save(notificacion);

            // Marcar pagos como liberados
            pagosPendientes.forEach(pago -> {
                pago.setLiberado(true);
                pagoRepository.save(pago);
            });
        }
    }

    // Realiza el reembolso del coste la participación del usuario cuando este la cancela.
    @Transactional
    public void reembolsarPagoCancelacionUsuario(Actividad actividad, Usuario usuario){
        Pago pagoAReembolsar = pagoRepository.findByActividadAndUsuarioAndReembolsadoFalse(actividad, usuario);
        pagoAReembolsar.setObservaciones("Reembolso de " + pagoAReembolsar.getCantidad()
                + " por cancelación de participación a la actividad " + actividad.getTitulo());
        pagoAReembolsar.setReembolsado(true);
        pagoRepository.save(pagoAReembolsar);
    }

    // Reembolsa los pagos a los usuarios cuando el creador cancela una actividad
    @Transactional
    public void reembolsarPagosCancelacionCreador(Actividad actividad) {
        List<Pago> pagosPendientes = pagoRepository.findByActividadAndLiberadoFalse(actividad);
        if (!pagosPendientes.isEmpty()) {
            pagosPendientes.forEach(pago -> {
                pago.setReembolsado(true);
                pago.setObservaciones("Devolución por actividad cancelada.");
                pagoRepository.save(pago);
            });
        }
    }

}
