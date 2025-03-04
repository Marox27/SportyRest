package com.example.SportyRest.service;

import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Equipo_miembro;
import com.example.SportyRest.model.Notificacion;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.EquipoMiembroRepository;
import com.example.SportyRest.repository.EquipoRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoMiembroService {

    @Autowired
    private EquipoMiembroRepository equipoMiembroRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private NotificacionService notificacionService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Equipo_miembro crearMiembro(Equipo_miembro equipoMiembro) {
        // Guardamos el nuevo miembro
        Equipo_miembro equipo_miembro_guardado = equipoMiembroRepository.save(equipoMiembro);

        //Sí el guardado es exitoso actualizamos el número de miembros del equipo asociado
        Equipo equipo = equipoMiembro.getEquipo();
        int numeroDeMiembros = equipoMiembroRepository.countByEquipo(equipo);
        equipo.setMiembros(numeroDeMiembros);
        equipoRepository.saveAndFlush(equipo);

        Usuario nuevo_miembro = equipoMiembro.getUsuario();
        Usuario lider = equipoMiembroRepository.findByEquipoAndRol(equipo, Equipo_miembro.Rol.ADMIN).getUsuario();

        // Envía una notificación al lider, de que se ha unido un miembro a su equipo
        Notificacion notificacion = new Notificacion();
        notificacion.setEmisor(nuevo_miembro);
        notificacion.setReceptor(lider);
        notificacion.setTitulo("Unión al equipo");
        notificacion.setMensaje("¡" + nuevo_miembro.getNickname() + " se ha unido al equipo " + equipo.getNombre() + "!");
        notificacionService.crearNotificacion(notificacion);

        // Por último devolvemos los datos del miembro
        return equipo_miembro_guardado;
    }

    public List<Equipo_miembro> obtenerMiembrosPorEquipo(Equipo equipoId) {
        return equipoMiembroRepository.findByEquipo(equipoId);
    }

    public List<Equipo_miembro> obtenerMiembrosPorUsuario(Usuario usuario) {
        return equipoMiembroRepository.findByUsuario(usuario);
    }

    public List<Usuario> obtenerUsuariosporEquipo(int equipo){
        return equipoMiembroRepository.findUsuariosByEquipoId(equipo);
    }

    public List<Equipo> obtenerEquiposPorUsuario(int usuario){
        return equipoMiembroRepository.findEquiposByUsuarioId(usuario);
    }

    // Obtener equipos donde un usuario es miembro
    public List<Equipo> obtenerEquiposPorUsuarioMiembro(int usuario){
        return equipoMiembroRepository.findEquiposDondeUsuarioEsMiembro(usuario);
    }

    public Optional<Equipo_miembro> obtenerMiembroPorId(int id) {
        return equipoMiembroRepository.findById(id);
    }

    @Transactional
    public void eliminarMiembro(int id) {
        Equipo_miembro miembro = equipoMiembroRepository.findByIdMiembro(id);
        Usuario miembroEliminado = miembro.getUsuario();
        Usuario lider = equipoMiembroRepository.findByEquipoAndRol(miembro.getEquipo(), Equipo_miembro.Rol.ADMIN).getUsuario();

        // Envía una notificación al lider, de que se ha unido un miembro a su equipo
        Notificacion notificacion = new Notificacion();
        notificacion.setEmisor(miembroEliminado);
        notificacion.setReceptor(lider);
        notificacion.setTitulo("Abandono en el equipo");
        notificacion.setMensaje("¡" + miembroEliminado.getNickname() + " ha abandonado el equipo " + miembro.getEquipo().getNombre() + "!");
        notificacionService.crearNotificacion(notificacion);

        equipoMiembroRepository.delete(miembro);
        Equipo equipo = miembro.getEquipo();
        int numeroDeMiembros = equipoMiembroRepository.countByEquipo(equipo);
        equipo.setMiembros(numeroDeMiembros);
        equipoRepository.save(equipo);
    }

    @Transactional
    public void eliminarMiembroYEquiposUsuarioEliminado(Usuario usuario){
        // Obtenemos todos las participaciones que tiene el usuario en diferentes equipos
        List<Equipo_miembro> equipoMiembros = equipoMiembroRepository.findByUsuario(usuario);

        // Si el usuario es lider de algun equipo se traslada el lider de haber mas miembros si no se elimina.
        // Después de eso se elimina al usuario del equipo
        for (Equipo_miembro equipoMiembro : equipoMiembros) {
            Equipo equipo = equipoMiembro.getEquipo();
            List<Equipo_miembro> miembrosDelEquipo = equipoMiembroRepository.findByEquipo(equipo);
            if (miembrosDelEquipo.size() > 1){
                equipo.setMiembros(equipo.getMiembros() - 1);
                equipoRepository.save(equipo);
            }

            if (equipoMiembro.getRol().equals(Equipo_miembro.Rol.ADMIN)) {
                if (miembrosDelEquipo.size() > 1) {
                    for (Equipo_miembro miembro : miembrosDelEquipo) {
                        if (!miembro.getUsuario().getId().equals(usuario.getId())) {
                            miembro.setRol(Equipo_miembro.Rol.ADMIN);
                            equipoMiembroRepository.save(miembro);
                            break;
                        }
                    }
                }
            }
            equipoMiembroRepository.delete(equipoMiembro); // Eliminar usuario del equipo

            // Si después de eliminarlo no quedan miembros, eliminamos el equipo
            if (equipoMiembroRepository.findByEquipo(equipo).isEmpty()) {
                equipoRepository.delete(equipo);
            }
        }

    }

}

