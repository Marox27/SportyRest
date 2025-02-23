package com.example.SportyRest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private int idNotificacion;
    @ManyToOne
    @JoinColumn(name = "emisor", nullable = false)
    private Usuario emisor;
    @ManyToOne
    @JoinColumn(name = "receptor", nullable = false)
    private Usuario receptor;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String mensaje;
    @Column(nullable = false)
    private boolean leida = false;
    @Column(name = "fecha_creacion",nullable = false)
    private String fechaCreacion = LocalDateTime.now().toString();

    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }
}
