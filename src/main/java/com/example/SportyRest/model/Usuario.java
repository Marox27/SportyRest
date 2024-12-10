package com.example.SportyRest.model;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idusuario;
    private String nombre;
    private String apellidos;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String mail;
    private String telefono;
    private String password;
    private String ciudad;
    private String pfp;
    private boolean is_admin;
    private boolean activo;

    // Getters y setters
    public Long getId() {
        return idusuario;
    }

    public void setId(Long id) {
        this.idusuario = id;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    // Nickmame
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Teléfono
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {this.telefono = telefono;}

    // Password
    public String getPassword(){
        return password;
    }
    public Boolean checkPassword(String userInputPassword) {
        String hashedPassword = getPassword();
        return BCrypt.checkpw(userInputPassword, hashedPassword);
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public String getEmail() {
        return mail;
    }

    public void setEmail(String mail) {
        this.mail = mail;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // PFP
    public String getPfp() {
        return pfp;
    }

    // De entrada debe de recibir el enlace resultante al subir la imagen.
    public void setpfp(String pfp) {
        this.pfp = pfp;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}


