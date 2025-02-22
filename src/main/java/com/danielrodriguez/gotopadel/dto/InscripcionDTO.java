package com.danielrodriguez.gotopadel.dto;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;

public class InscripcionDTO {

    private Usuario usuario;
    private Partido partido;
    private String estado; // Campo añadido para el estado

    public InscripcionDTO(Usuario usuario, Partido partido, String estado) {
        this.usuario = usuario;
        this.partido = partido;
        this.estado = (estado != null) ? estado : null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
