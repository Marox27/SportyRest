package com.example.SportyRest.controller;

import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.service.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/create")
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario usuario) {
        Usuario newUser = usuarioService.createUser(usuario);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/updatePass")
    public ResponseEntity<Usuario> updatePassword(@RequestParam String nickname, @RequestParam String password){
        System.out.println(nickname + " " + password);
        Usuario usuario = usuarioService.getUserByNickname(nickname);
        usuarioService.updatePassword(usuario, password);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Usuario>> listUsers(){
        List<Usuario> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/user")
    public ResponseEntity<Usuario>getUser(@RequestParam int id){
        Usuario usuario = usuarioService.getUserById(id);
        return ResponseEntity.ok(usuario);
    }

}

