package com.danielrodriguez.gotopadel.dto;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;

/**
 * Data Transfer Object (DTO) para representar la inscripción de un usuario en un partido.
 * 
 * Esta clase encapsula la información sobre un usuario inscrito en un partido, incluyendo
 * el estado de la inscripción. Se utiliza para transferir esta información entre las capas
 * de la aplicación de forma estructurada.
 */
public class InscripcionDTO {

    private Usuario usuario;
    private Partido partido;
    

     /**
     * Constructor de la clase InscripcionDTO.
     * 
     * Este constructor crea un objeto con los valores proporcionados.
     * Si el estado es null, se establecerá también como null.
     *
     * @param usuario El usuario inscrito en el partido.
     * @param partido El partido en el que el usuario está inscrito.     
     */
    public InscripcionDTO(Usuario usuario, Partido partido) {
        this.usuario = usuario;
        this.partido = partido;       
    }

    /**
     * Obtiene el usuario inscrito en el partido.
     *
     * @return El usuario inscrito en el partido.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario inscrito en el partido.
     *
     * @param usuario El usuario a establecer como inscrito en el partido.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el partido al que el usuario está inscrito.
     *
     * @return El partido al que el usuario está inscrito.
     */
    public Partido getPartido() {
        return partido;
    }

    /**
     * Establece el partido al que el usuario está inscrito.
     *
     * @param partido El partido en el que se desea inscribir al usuario.
     */
    public void setPartido(Partido partido) {
        this.partido = partido;
    }
    
}
