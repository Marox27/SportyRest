package com.example.SportyRest.controller;

import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/todas")
    public List<Notificacion> obtenerTodasNotificaciones(@RequestParam int idUsuario) {
        return notificacionService.getNotificacionesByUsuario(idUsuario);
    }

    @GetMapping("/no-leidas")
    public List<Notificacion> obtenerNotificacionesNoLeidas(@RequestParam int idUsuario) {
        return notificacionService.getNotificacionesByUsuarioNoLeidas(idUsuario);
    }

    @PutMapping("/marcar-leidas")
    public ResponseEntity<Boolean> marcarTodasLeidas(@RequestBody List<Integer> ids) {
        boolean ok = notificacionService.marcarNotificacionesComoLeidas(ids);
        return new ResponseEntity<>(ok, HttpStatus.OK);
    }




    @PostMapping("/crear")
    public Notificacion crearNotificacion(@RequestBody Notificacion notificacion) {
        return notificacionService.crearNotificacion(notificacion);
    }


}

