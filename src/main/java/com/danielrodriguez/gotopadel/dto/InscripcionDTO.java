package com.danielrodriguez.gotopadel.dto;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;

public class InscripcionDTO {

    private Usuario usuario;
    private Partido partido;

    public InscripcionDTO(Usuario usuario, Partido partido) {
        this.usuario = usuario;
        this.partido = partido;
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

}
