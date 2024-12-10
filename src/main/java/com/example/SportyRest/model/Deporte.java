package com.example.SportyRest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "deporte")
public class Deporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddeporte")
    private int idDeporte;

    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;

    @Column(name = "normativa", length = 1000, nullable = false)
    private String normativa;

    @Column(name = "min_jugadores", nullable = false)
    private int minJugadores;

    @Column(name = "max_jugadores", nullable = false)
    private int maxJugadores;

    // Getters y Setters
    public int getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNormativa() {
        return normativa;
    }

    public void setNormativa(String normativa) {
        this.normativa = normativa;
    }

    public int getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(int minJugadores) {
        this.minJugadores = minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }
}
