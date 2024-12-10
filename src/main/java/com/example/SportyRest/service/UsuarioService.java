package com.example.SportyRest.service;

import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario createUser(Usuario usuario) {
        // Hashear la contraseña antes de guardar
        usuario.setPassword(usuario.getPassword());
        System.out.println("Usuario " + usuario.getNickname() + " creado con éxito");
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

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll(); // Usa el método findAll() de JpaRepository
    }

    public Usuario getUserByNickname(String nickname){
        return usuarioRepository.findByNickname(nickname);
    }

}
