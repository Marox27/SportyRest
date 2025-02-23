package com.example.SportyRest.controller;

import com.example.SportyRest.model.LoginRequest;
import com.example.SportyRest.model.LoginResponse;
import com.example.SportyRest.model.Usuario;
import com.example.SportyRest.service.AuthService;
import com.example.SportyRest.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Usuario user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println(user);
        Map<String, Object> claims = new HashMap<>();
        if (user == null){
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        if (user.isActivo()) {
            if (user.isIs_admin()) {
                claims.put("role", "ADMIN");
                claims.put("isAdmin", true);
                String token = jwtTokenUtil.generateToken(user.getNickname(), claims);
                return ResponseEntity.ok(new LoginResponse(user.getId().intValue(), token));
            } else {
                claims.put("role", "USER");
                claims.put("isAdmin", false);
                String token = jwtTokenUtil.generateToken(user.getNickname(), claims);
                return ResponseEntity.ok(new LoginResponse(user.getId().intValue(), token));
            }
        }
        return ResponseEntity.status(403).body("Usuario inactivo");
    }

    @GetMapping("validar-token")
    public ResponseEntity<Boolean> validarToken(@RequestParam String token) {
        return ResponseEntity.status(403).body(jwtTokenUtil.validateToken(token));
    }

}

