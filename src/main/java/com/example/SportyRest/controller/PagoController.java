package com.example.SportyRest.controller;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Pago;
import com.example.SportyRest.repository.ActividadRepository;
import com.example.SportyRest.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    @Autowired
    private PagoService pagoService;

    @Autowired
    private ActividadRepository actividadRepository;

    @PostMapping
    public Pago realizarPago(@RequestBody Pago pago) {
        return pagoService.registrarPago(pago);
    }

    @PostMapping ("/reembolsar")
    public Pago realizarReembolso(@RequestBody Pago pago) {
        pago.setReembolsado(true);
        return pagoService.registrarPago(pago);
    }

    @PostMapping("/liberar/{idActividad}")
    public Boolean liberarPagos(@PathVariable int idActividad) {
        Actividad actividad = actividadRepository.findById(idActividad);

        if (actividad != null) {
            pagoService.liberarPagos(actividad);
            return true;
        } else {
            return false;
        }
    }

    @GetMapping
    private ResponseEntity<List<Pago>> obtenerPagosDeUsuario(@RequestParam int idUsuario){
        return new ResponseEntity<>(pagoService.obtenerPagosDeUsuario(idUsuario), HttpStatus.OK);
    }

}

