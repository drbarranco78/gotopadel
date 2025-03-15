package com.danielrodriguez.gotopadel.dto;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) para representar una notificación.
 * 
 * Esta clase encapsula la información relacionada con una notificación, como el emisor,
 * el mensaje, el tipo de notificación y la fecha de creación. Se utiliza para transferir
 * esta información entre las capas de la aplicación de forma estructurada.
 */

public class NotificacionDTO {
    private Integer id;
    private Usuario emisor;
    private Partido partido;
    private String tipo;
    private String mensaje;
    private LocalDateTime fechaCreacion;

    
    /**
     * Constructor de la clase NotificacionDTO.
     * 
     * Este constructor crea un objeto con los valores proporcionados.
     * 
     * @param id El ID de la notificación.
     * @param emisor El usuario que emite la notificación.
     * @param mensaje El mensaje de la notificación.
     * @param tipo El tipo de la notificación
     * @param fechaCreacion La fecha de creación de la notificación.
     */
    public NotificacionDTO(Integer id, Usuario emisor, String mensaje, String tipo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.emisor = emisor;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
    }    

    /**
     * Obtiene el ID de la notificación.
     *
     * @return El ID de la notificación.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Obtiene el emisor de la notificación.
     *
     * @return El usuario que emitió la notificación.
     */
    public Usuario getEmisor() {
        return emisor;
    }

    /**
     * Obtiene el partido asociado a la notificación.
     *
     * @return El partido relacionado con la notificación.
     */
    public Partido getPartido() {
        return partido;
    }

    /**
     * Obtiene el tipo de la notificación.
     *
     * @return El tipo de la notificación (por ejemplo, "informativa", "alerta").
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el mensaje de la notificación.
     *
     * @return El mensaje contenido en la notificación.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Obtiene la fecha de creación de la notificación.
     *
     * @return La fecha y hora en que se creó la notificación.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
