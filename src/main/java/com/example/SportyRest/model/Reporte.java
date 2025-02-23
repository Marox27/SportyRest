package com.example.SportyRest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_reporte", nullable = false)
    private TipoDeReporte tipoDeReporte;
    private String motivo;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_reportante", nullable = false)
    private Usuario usuarioReportante;

    @ManyToOne
    @JoinColumn(name = "usuario_reportado")
    private Usuario usuarioReportado;

    @ManyToOne
    @JoinColumn(name = "actividad_reportada")
    private Actividad actividadReportada;

    @ManyToOne
    @JoinColumn(name = "equipo_reportado")
    private Equipo equipoReportado;

    private String fechaCreacion = LocalDateTime.now().toString();
    private boolean revisado = false;

    public enum TipoDeReporte {
        ACTIVIDAD, USUARIO, EQUIPO
    }

    // Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoDeReporte getTipoDeReporte() {
        return tipoDeReporte;
    }

    public void setTipoDeReporte(TipoDeReporte tipoDeReporte) {
        this.tipoDeReporte = tipoDeReporte;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuarioReportante() {
        return usuarioReportante;
    }

    public void setUsuarioReportante(Usuario usuarioReportante) {
        this.usuarioReportante = usuarioReportante;
    }

    public Usuario getUsuarioReportado() {
        return usuarioReportado;
    }

    public void setUsuarioReportado(Usuario usuarioReportado) {
        this.usuarioReportado = usuarioReportado;
    }

    public Actividad getActividadReportada() {
        return actividadReportada;
    }

    public void setActividadReportada(Actividad actividadReportada) {
        this.actividadReportada = actividadReportada;
    }

    public Equipo getEquipoReportado() {
        return equipoReportado;
    }

    public void setEquipoReportado(Equipo equipoReportado) {
        this.equipoReportado = equipoReportado;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }

}
