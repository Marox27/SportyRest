package com.example.SportyRest.controller;

import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.service.EmailService;
import com.example.SportyRest.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/create")
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario usuario) {
        Usuario newUser = usuarioService.createUser(usuario);
        System.out.println(usuario.getFecha_nacimiento());
        if (newUser != null){
            emailService.sendEmailWithAttachment(newUser.getEmail(), "Confirmación de Registro",
                    "Bienvenido a SportyHub " + newUser.getName() + ", tu cuenta con nombre de usuario " + newUser.getNickname() +
                            " ha sido creada correctamente. Con esto ya está todo listo para la acción ¡Te esperamos en la cancha!");
        }
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/edit")
    public ResponseEntity<Boolean> updateUserInfo(@RequestBody Usuario usuario) {
        boolean exito = usuarioService.updateUsuarioInfo(usuario);
        return ResponseEntity.ok(exito);
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

    @GetMapping("/check-user")
    public ResponseEntity<Boolean>CheckUser(@RequestParam String mail){
        Usuario usuario = usuarioService.getUserByMail(mail);
        if (usuario == null){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/ban-user")
    public ResponseEntity<Boolean>banearUsuario(@RequestParam int idUsuario){
        Usuario usuario = usuarioService.getUserById(idUsuario);
        if (usuario == null){
            return ResponseEntity.ok(false);
        }
        usuarioService.banearUsuario(usuario);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/desban-user")
    public ResponseEntity<Boolean>desbanearUsuario(@RequestParam int idUsuario){
        Usuario usuario = usuarioService.getUserById(idUsuario);
        if (usuario == null){
            return ResponseEntity.ok(false);
        }
        usuarioService.desbanearUsuario(usuario);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean>eliminarCuenta(@RequestParam int idUsuario){
        boolean exito = usuarioService.eliminarCuenta(idUsuario);
        return ResponseEntity.ok(exito);
    }

}

