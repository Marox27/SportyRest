package com.example.SportyRest.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            "MondongoDeLaPraderaDelMonteSantisimaTrinidadLimon27".getBytes()  // Reemplaza con una clave más larga si usas HS512
    );

    // Valida si el token no ha expirado
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("El token ha expirado");
        } catch (MalformedJwtException e) {
            System.out.println("El token está mal formado");
        } catch (SignatureException e) {
            System.out.println("Firma del token no válida");
        } catch (Exception e) {
            System.out.println("Error al validar el token: " + e.getMessage());
        }
        return false;
    }


    // Extrae el nombre de usuario del token
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Extrae los reclamos (claims) del token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()               // Usa 'parserBuilder' en vez de 'parser'
                .setSigningKey(SECRET_KEY)       // Establece la clave secreta
                .build()
                .parseClaimsJws(token)          // Parsea el JWT
                .getBody();                      // Obtiene los claims
    }

    // Genera un token JWT
    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)            // Establece el 'subject' (nombre de usuario)
                .setIssuedAt(new Date())         // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira en 1 hora
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Firma con el algoritmo y la clave secreta
                .compact();                      // Genera el JWT
    }

    // Validar y extraer claims de un token
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Clave usada para firmar el token
                .build()
                .parseClaimsJws(token) // Validar y decodificar el token
                .getBody(); // Obtener los claims (payload)
    }

}


