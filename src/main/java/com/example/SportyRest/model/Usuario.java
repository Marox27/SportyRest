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
    private String password;
    @Column(name = "fecha_nacimiento")
    private String fecha_nacimiento;
    private String ciudad;
    private String pfp;
    private boolean is_admin;
    private boolean activo;
    private boolean baneado = false;

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


    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    // Password
    public String getPassword(){
        return password;
    }
    public Boolean checkPassword(String userInputPassword) {
        String hashedPassword = getPassword();
        return BCrypt.checkpw(userInputPassword, hashedPassword);
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isBaneado() {
        return baneado;
    }

    public void setBaneado(boolean baneado) {
        this.baneado = baneado;
    }
}


