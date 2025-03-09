package com.danielrodriguez.gotopadel.dto;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;

import java.time.LocalDateTime;

public class NotificacionDTO {
    private Integer id;
    private Usuario emisor;
    private Partido partido;
    private String tipo; // Nuevo campo para el tipo de notificación
    private LocalDateTime fechaCreacion;

    public NotificacionDTO(Integer id, Usuario emisor, Partido partido, String tipo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.emisor = emisor;
        this.partido = partido;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getId() {
        return id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public Partido getPartido() {
        return partido;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
