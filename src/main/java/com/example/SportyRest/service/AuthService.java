package com.example.SportyRest.service;

import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;  // Repositorio de usuarios
    private BCrypt passwordEncoder;

    public Usuario authenticate(String username, String password) {
        Usuario usuario = usuarioRepository.findByNickname(username);
        System.out.println(password + " " + usuario.getPassword());
        if (usuario != null && BCrypt.checkpw(password, usuario.getPassword())) {
            System.out.println("Authservice: " + username + password + " " + usuario.getPassword());
            System.out.println(BCrypt.checkpw(password, usuario.getPassword()));
            return usuario;
        }
        return null;
    }

    // Método para obtener un usuario por su nickname
    public Usuario getUserByNickname(String nickname) {
        Usuario usuario = usuarioRepository.findByNickname(nickname);
        if (usuario != null) {
            return usuario;
        } else {
            try{
                throw new NullPointerException();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            return usuario;
        }
    }
}

