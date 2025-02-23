package com.example.SportyRest.service;

import com.example.SportyRest.model.Actividad;
import com.example.SportyRest.model.Pago;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.ActividadRepository;
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
    private final PagoRepository pagoRepository;
    @Autowired
    private final ActividadRepository actividadRepository;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final PayPalService payPalService; // Este lo crearemos luego para la integración

    public PagoService(PagoRepository pagoRepository, ActividadRepository actividadRepository,
                       UsuarioRepository usuarioRepository, PayPalService payPalService) {
        this.pagoRepository = pagoRepository;
        this.actividadRepository = actividadRepository;
        this.usuarioRepository = usuarioRepository;
        this.payPalService = payPalService;
    }

    /*
    @Scheduled(fixedRate = 3600000) // Ejecuta cada hora
    @Transactional
    public void liberarPagos() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Actividad> acti
        List<Actividad> actividadesFinalizadas = actividadRepository.findByFechaHoraFinalizacionBefore(ahora);

        for (Actividad actividad : actividadesFinalizadas) {
            List<Pago> pagosPendientes = pagoRepository.findByActividadAndReembolsadoFalse(actividad);

            for (Pago pago : pagosPendientes) {
                boolean exito = payPalService.capturarPago(pago); // Llamamos a PayPal para liberar los pagos

                if (exito) {
                    pago.setReembolsado(true);
                    pagoRepository.save(pago);
                }
            }
        }
    }
*/
    public Pago registrarPago(Pago pago) {
        pago.setReembolsado(false);
        pago.setLiberado(false); // Se marca como retenido hasta que la actividad finalice
        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagosDeUsuario(int idUsuario){
        Usuario usuario = usuarioRepository.findByIdusuario(idUsuario);
        return pagoRepository.findByUsuario(usuario);
    }

    public void liberarPagos(Actividad actividad) {
        List<Pago> pagosPendientes = pagoRepository.findByActividadAndLiberadoFalse(actividad);

        if (!pagosPendientes.isEmpty()) {
            double total = pagosPendientes.stream().mapToDouble(Pago::getCantidad).sum();
            Usuario creador = usuarioRepository.findByIdusuario(actividad.getCreador());

            // Simulación de transferencia de dinero al creador
            System.out.println("Transferidos " + total + "€ al creador: " + creador.getNickname());

            // Marcar pagos como liberados
            pagosPendientes.forEach(pago -> {
                pago.setLiberado(true);
                pagoRepository.save(pago);
            });
        }
    }

}
