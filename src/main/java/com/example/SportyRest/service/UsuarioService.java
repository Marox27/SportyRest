package com.example.SportyRest.service;

import com.example.SportyRest.model.Equipo;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.EquipoMiembroRepository;
import com.example.SportyRest.repository.EquipoRepository;
import com.example.SportyRest.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        usuarioRepository.save(usuario);
    }
}
