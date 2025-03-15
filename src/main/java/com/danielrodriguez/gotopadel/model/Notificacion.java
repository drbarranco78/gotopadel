package com.danielrodriguez.gotopadel.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;


/**
 * Representa una notificación dentro de la aplicación.
 * Una notificación es enviada por un emisor a un receptor, con un mensaje y un tipo,
 * y contiene información sobre la fecha de creación.
 */
@Entity
@Table(name = "NOTIFICACIONES")
public class Notificacion implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_emisor", nullable = false)
    private Usuario emisor;

    @ManyToOne
    @JoinColumn(name = "id_receptor", nullable = false)
    private Usuario receptor;   

    @Column(nullable = true)
    private String tipo;

    @Column(nullable = true, columnDefinition = "TEXT") 
    private String mensaje;    

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Constructor por defecto para la entidad Notificacion.
     * Inicializa la fecha de creación con la fecha y hora actuales.
     */
    public Notificacion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Constructor para crear una nueva notificación con los valores proporcionados.
     *
     * @param emisor El usuario que emite la notificación.
     * @param receptor El usuario que recibe la notificación.
     * @param mensaje El mensaje de la notificación.
     * @param tipo El tipo de notificación (opcional).
     */
    public Notificacion(Usuario emisor, Usuario receptor, String mensaje, String tipo) {
        this.emisor = emisor;
        this.receptor = receptor;        
        this.mensaje = mensaje;
        this.tipo = tipo;        
        this.fechaCreacion = LocalDateTime.now();
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
     * Establece el ID de la notificación.
     *
     * @param id El ID de la notificación.
     */
    public void setId(Integer id) {
        this.id = id;
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
     * Establece el emisor de la notificación.
     *
     * @param emisor El usuario que emitirá la notificación.
     */
    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }
    /**
     * Obtiene el receptor de la notificación.
     *
     * @return El usuario que recibirá la notificación.
     */
    public Usuario getReceptor() {
        return receptor;
    }

    /**
     * Establece el receptor de la notificación.
     *
     * @param receptor El usuario que recibirá la notificación.
     */
    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }    

    /**
     * Obtiene el tipo de la notificación.
     *
     * @return El tipo de la notificación, o {@code null} si no se ha especificado.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la notificación.
     *
     * @param tipo El tipo de la notificación.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * Obtiene la fecha de creación de la notificación.
     *
     * @return La fecha y hora en que se creó la notificación.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación de la notificación.
     *
     * @param fechaCreacion La fecha de creación de la notificación.
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el mensaje de la notificación.
     *
     * @return El mensaje de la notificación.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje de la notificación.
     *
     * @param mensaje El mensaje de la notificación.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
}