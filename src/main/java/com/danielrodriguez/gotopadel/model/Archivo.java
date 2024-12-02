package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARCHIVO") 
public class Archivo {

    @Id
    @Column(name = "id_partido", nullable = false)
    private Integer idPartido;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "tipo_partido", nullable = false, length = 10)
    private String tipoPartido;

    @Column(name = "nivel", length = 20)
    private String nivel;

    @Column(name = "fecha_partido", length = 10)
    private String fechaPartido;

    @Column(name = "fecha_publicacion", length = 10)
    private String fechaPublicacion;

    @Column(name = "hora_partido", length = 6)
    private String horaPartido;

    @Column
    private Integer ubicacion;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "fecha_archivo", length = 10)
    private String fechaArchivo;

    @Column(name = "motivo_archivado", length = 30)
    private String motivoArchivado;

    // Getters y setters
    public Integer getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Integer idPartido) {
        this.idPartido = idPartido;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoPartido() {
        return tipoPartido;
    }

    public void setTipoPartido(String tipoPartido) {
        this.tipoPartido = tipoPartido;
    }

    

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getHoraPartido() {
        return horaPartido;
    }

    public void setHoraPartido(String horaPartido) {
        this.horaPartido = horaPartido;
    }

    public Integer getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Integer ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getFechaArchivo() {
        return fechaArchivo;
    }

    public void setFechaArchivo(String fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    public String getMotivoArchivado() {
        return motivoArchivado;
    }

    public void setMotivoArchivado(String motivoArchivado) {
        this.motivoArchivado = motivoArchivado;
    }
}



