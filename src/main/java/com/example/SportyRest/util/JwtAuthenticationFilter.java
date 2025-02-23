package com.example.SportyRest.util;

import com.example.SportyRest.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Set<String> RUTAS_PERMITIDAS = Set.of(
            "/api/login",
            "/api/verification/send-code",
            "/api/verification/verify",
            "/api/provincias",
            "/api/usuarios/create",
            "/api/usuarios/check-user",
            "/api/equipos/subirImagen"
    );

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();


        // Permitir el acceso a ciertas rutas públicas sin token
        if (RUTAS_PERMITIDAS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        // Obtener el encabezado Authorization
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token no presente o inválido");
            return;
        }

        // Extraer el token
        String token = authorizationHeader.substring(7);

        // Validar el token
        try {
            Claims claims = JwtTokenUtil.parseToken(token); // Método para validar y obtener claims
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException ex) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Token inválido
            return;
        }

        // Si el token es válido, pasa al siguiente filtro
        filterChain.doFilter(request, response);
    }
}
