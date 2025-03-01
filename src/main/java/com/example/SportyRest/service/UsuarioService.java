package com.example.SportyRest.service;

import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.EquipoMiembroRepository;
import com.example.SportyRest.repository.EquipoRepository;
import com.example.SportyRest.repository.ParticipanteRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private EquipoMiembroRepository equipoMiembroRepository;
    @Autowired
    private EquipoMiembroService equipoMiembroService;
    @Autowired
    private ParticipanteService participanteService;
    @Autowired
    private ActividadService actividadService;

    public Usuario createUser(Usuario usuario) {
        // Hashear la contraseña antes de guardar
        String user_pass = usuario.getPassword();
        String hashed_pass = BCrypt.hashpw(user_pass, BCrypt.gensalt());
        usuario.setPassword(hashed_pass);
        System.out.println("Usuario " + usuario.getNickname() + " creado con éxito" + user_pass);
        try {
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Puedes lanzar la excepción o manejarla según lo necesites
        }
    }

    public boolean updateUsuarioInfo(Usuario usuario){
        Usuario usuarioOriginal = usuarioRepository.findByIdusuario(usuario.getId().intValue());
        usuarioOriginal.setName(usuario.getName());
        usuarioOriginal.setApellidos(usuario.getApellidos());
        usuarioOriginal.setCiudad(usuario.getCiudad());
        usuarioOriginal.setpfp(usuario.getPfp());
        usuarioRepository.save(usuarioOriginal);
        return true;
    }

    public void updatePassword(Usuario usuario, String password){
        usuario.setPassword(password);
        usuarioRepository.save(usuario);
    }

    public Usuario deleteUser(Usuario usuario){
        usuario.setActivo(false);
        manejarEliminacionUsuario(usuario);
        return usuarioRepository.save(usuario);
    }

    public void manejarEliminacionUsuario(Usuario usuario) {
        if (!usuario.isActivo()) {
            List<Equipo> equipos = equipoMiembroRepository.findEquiposByUsuarioId(usuario.getId().intValue());

            for (Equipo equipo : equipos) {
                Optional<Usuario> nuevoCreador = equipoMiembroRepository.findUsuariosByEquipoId(equipo.getIdequipo()).stream()//equipo.getMiembros().stream()
                        .filter(Usuario::isActivo)
                        .findFirst();

                if (nuevoCreador.isPresent()) {
                    equipo.setCreador(nuevoCreador.get().getId().intValue());
                } else {
                    equipo.setActivo(false); // Desactivar equipo si no hay miembros activos
                }
                equipoRepository.save(equipo);
            }
            System.out.println("Acciones para la eliminación de usuario realizadas...");
        }
    }


    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll(); // Usa el método findAll() de JpaRepository
    }

    public Usuario getUserByNickname(String nickname){
        return usuarioRepository.findByNickname(nickname);
    }

    public Usuario getUserById(int id){return usuarioRepository.findByIdusuario(id);}

    public Usuario getUserByMail(String mail){return usuarioRepository.findByMail(mail);}

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void banearUsuario(Usuario usuario){
        usuario.setBaneado(true);
        participanteService.cancelarParticipacionesUsuarioBaneado(usuario);
        usuarioRepository.save(usuario);
    }

    public void desbanearUsuario(Usuario usuario){
        usuario.setBaneado(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Boolean eliminarCuenta(int idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

        if (usuario.isPresent()){
            Usuario usuarioAEliminar = usuario.get();
            // Cancela y elimina las actividades creadas por el  usuario
            actividadService.cancelarActividadesUsuarioEliminado(usuarioAEliminar.getId());
            // Cancela las participaciones del usuario en equipos
            equipoMiembroService.eliminarMiembroYEquiposUsuarioEliminado(usuarioAEliminar);
            // Cancela las participaciones del usuario en actividades activas
            participanteService.cancelarParticipacionesUsuarioBaneado(usuarioAEliminar);

            // Generar email y nickname únicos basados en el ID del usuario
            String nuevoEmail = "deleted_" + idUsuario + "@eliminado.com";
            String nuevoNickname = "deleted_" + idUsuario;

            // Anonimizar datos del usuario
            usuarioAEliminar.setActivo(false);
            usuarioAEliminar.setEmail(nuevoEmail);
            usuarioAEliminar.setNickname(nuevoNickname);
            usuarioAEliminar.setpfp("https://res.cloudinary.com/dkl7y8jew/image/upload/v1740791519/defaul_pfp.jpg");
            usuarioAEliminar.setName("Cuenta Eliminada"); // Opcional

            usuarioRepository.save(usuarioAEliminar);
        }
        return false;
    }


}
