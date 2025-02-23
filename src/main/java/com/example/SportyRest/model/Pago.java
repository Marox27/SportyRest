package com.example.SportyRest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Pago {
    @Id
    @Column(name = "id_pago")
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "actividad", nullable = false)
    private Actividad actividad;

    @Column(nullable = false)
    private double cantidad;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_pago", nullable = false)
    private String fechaPago = LocalDateTime.now().toString();

    @Column(nullable = false)
    private boolean reembolsado = false;

    @Column(nullable = false)
    private boolean liberado = false; // Nuevo campo para marcar pagos liberados

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public boolean isReembolsado() {
        return reembolsado;
    }

    public void setReembolsado(boolean reembolsado) {
        this.reembolsado = reembolsado;
    }

    public boolean isLiberado() {
        return liberado;
    }

    public void setLiberado(boolean liberado) {
        this.liberado = liberado;
    }
}