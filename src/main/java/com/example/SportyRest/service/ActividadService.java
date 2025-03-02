package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Participante;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.repository.NotificacionRepository;
import com.example.SportyRest.repository.ParticipanteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ParticipanteRepository participanteRepository;
    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private PagoService pagoService;
    @Autowired
    private NotificacionService notificacionService;

    // Crear actividad
    @Transactional
    public void crearActividad(Actividad actividad) {
        // Verifica que la actividad tenga un ID asignado después de guardarla
        actividad.setFecha_publicacion(LocalDate.now().toString()); // Establece la fecha de publicación
        actividad.setActivo(true); // Activa la actividad por defecto

        // Guardar la actividad
        actividadRepository.save(actividad);
    }


    // Eliminar actividad por ID
    @Transactional
    public boolean eliminarActividad(int idActividad) {
        Optional<Actividad> actividad = Optional.ofNullable(actividadRepository.findById(idActividad));
        if (actividad.isPresent()) {
            actividadRepository.deleteById(idActividad);
            return true;
        }
        return false; // Actividad no encontrada
    }

    public List<Actividad> getTodasActividades() {
        return actividadRepository.findByActivoTrue();
    }

    public List<Actividad> getActividadesCercanas(double latitud, double longitud, double distancia) {
        List<Actividad> actividadesCercanas = actividadRepository.findActividadesCercanas(latitud, longitud, distancia);
        System.out.println("Actividades Cercanas: " + actividadesCercanas.size());
        return actividadesCercanas;
    }

    public List<Actividad> getActividadesPorUsuario(Long idUsuario) {
        return participanteRepository.findActividadesByUsuarioId(idUsuario.intValue());
    }

    public List<Actividad> getActividadesCreadasPorUsuario(Long idUsuario) {
        return actividadRepository.findByCreador(idUsuario);
    }

    // Acciones cuando el usuario cancela una actividad
    @Transactional
    public Boolean cancelarActividad(int idActividad){
        Optional<Actividad> actividad = Optional.ofNullable(actividadRepository.findById(idActividad));

        if (actividad.isPresent()){
            // Se envía una notificación a todos los participantes de la actividad cancelada.
            notificacionService.enviarNotificacionesActividadCancelada(actividad.get());
            pagoService.reembolsarPagosCancelacionCreador(actividad.get());
            actividadRepository.delete(actividad.get());
            return true;
        }
        return false;
    }

    // Acciones cuando el usuario elimina su cuenta y estaba participando en alguna actividad. Se eliminan sus participaciones pendientes.
    @Transactional
    public void cancelarActividadesUsuarioEliminado(Long idUsuario){
        List<Actividad> actividadList = actividadRepository.findByCreadorAndActivoTrue(idUsuario);
        if (actividadList != null && !actividadList.isEmpty()){
            for (Actividad actividad: actividadList) {
                cancelarActividad(actividad.getIdactividad());
            }
        }
    }

    // Se cancela la actividad, las participaciones y se reembolsa el importe de las participaciones (si hubiera) por falta de participantes.
    @Transactional
    public void cancelarActividadFaltaParticipantes(Actividad actividad){
        // Se envía una notificación a todos los participantes de la actividad cancelada.
        notificacionService.enviarNotificacionesActividadCanceladaAutomatica(actividad);
        pagoService.reembolsarPagosCancelacionCreador(actividad);
        actividadRepository.delete(actividad);
    }

    // Da por finalizadas las actividades que han cumplido el plazo establecido
    @Scheduled(fixedRate = 300000) // Cada 5 minutos
    @Transactional
    public void validarActividadesFinalizadas() {
        //Obtener todas las actividades activas
        List<Actividad> actividades = actividadRepository.findByActivoTrue();

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Actividad actividad : actividades) {
            // Convertir fecha y hora a LocalDateTime
            String fechaHoraStr = actividad.getFecha() + " " + actividad.getHora();
            LocalDateTime fechaHoraActividad = LocalDateTime.parse(fechaHoraStr, formatterFecha);

            // Calcular la hora de finalización
            LocalDateTime fechaHoraFin = fechaHoraActividad.plusMinutes(actividad.getDuracion());

            if (ahora.isAfter(fechaHoraActividad) && actividad.getNum_participantes() < participanteRepository.countByActividad(actividad)){
                cancelarActividadFaltaParticipantes(actividad);
            }

            // Si la actividad ya terminó, desactivarla
            if (ahora.isAfter(fechaHoraFin)) {
                actividad.setActivo(false);
                actividadRepository.save(actividad);

                // Liberar pagos y notificar a los usuarios
                pagoService.liberarPagos(actividad);
                notificacionService.enviarNotificacionesFinActividad(actividad);
                System.out.println("Actividad con ID " + actividad.getIdactividad() + " marcada como inactiva.");
            }
        }
    }


}

