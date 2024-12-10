package com.example.SportyRest.model;

public class LoginResponse {
    private int id_usuario;
    private String token;

    // Constructor
    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse(int id_usuario, String token){
        this.id_usuario = id_usuario;
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter
    public void setToken(String token) {
        this.token = token;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    // Setter
    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }


}

