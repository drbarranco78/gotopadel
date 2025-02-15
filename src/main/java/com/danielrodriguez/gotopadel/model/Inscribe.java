package com.danielrodriguez.gotopadel.model;

import java.io.Serializable;

import jakarta.persistence.*;

/**
 * Representa la relación entre un usuario y un partido, indicando que un
 * usuario se ha inscrito en un partido.
 * Esta entidad está mapeada a la tabla "INSCRIBE" en la base de datos.
 */
@Entity
@Table(name = "INSCRIBE")
public class Inscribe implements Serializable {

    

    /**
     * Identificador único de la inscripción.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Integer idInscripcion;

    /**
     * Usuario que se ha inscrito en el partido.
     * Es una relación @ManyToOne, ya que un usuario puede inscribirse en varios
     * partidos.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Partido en el que el usuario se ha inscrito.
     * Es una relación @ManyToOne, ya que un partido puede tener varias
     * inscripciones.
     */
    @ManyToOne
    @JoinColumn(name = "id_partido", nullable = false)
    private Partido partido;

    /**
     * Fecha en la que el usuario se inscribió en el partido.
     * Este campo es opcional y se almacena como una cadena de texto.
     */
    @Column(name = "fecha_ins")
    private String fechaIns;

    /**
     * Indica si el organizador del partido ha sido notificado de la inscripción.
     * Este campo se inicializa en {@code false} y se actualiza a {@code true}
     * cuando la notificación ha sido enviada.
     */
    private boolean notificado = false;

    // Constructores, getters y setters

    /**
     * Constructor vacío requerido para JPA.
     */
    public Inscribe() {
    }

    /**
     * Constructor que permite inicializar una inscripción con el usuario, el
     * partido y la fecha de inscripción.
     *
     * @param usuario  Usuario que se inscribe.
     * @param partido  Partido en el que se inscribe el usuario.
     * @param fechaIns Fecha de inscripción.
     */
    public Inscribe(Usuario usuario, Partido partido, String fechaIns) {
        this.usuario = usuario;
        this.partido = partido;
        this.fechaIns = fechaIns;
    }

    /**
     * Obtiene el identificador único de la inscripción.
     *
     * @return el identificador de la inscripción.
     */
    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    /**
     * Establece el identificador único de la inscripción.
     *
     * @param idInscripcion el identificador de la inscripción.
     */
    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    /**
     * Obtiene el usuario que se inscribió en el partido.
     *
     * @return el usuario inscrito.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que se inscribió en el partido.
     *
     * @param usuario el usuario inscrito.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el partido en el que se inscribió el usuario.
     *
     * @return el partido de la inscripción.
     */
    public Partido getPartido() {
        return partido;
    }

    /**
     * Establece el partido en el que se inscribió el usuario.
     *
     * @param partido el partido de la inscripción.
     */
    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    /**
     * Obtiene la fecha en la que el usuario se inscribió en el partido.
     *
     * @return la fecha de inscripción.
     */
    public String getFechaIns() {
        return fechaIns;
    }

    /**
     * Establece la fecha en la que el usuario se inscribió en el partido.
     *
     * @param fechaIns la fecha de inscripción.
     */
    public void setFechaIns(String fechaIns) {
        this.fechaIns = fechaIns;
    }

    /**
     * Obtiene el estado de notificación de la inscripción.
     *
     * @return true si el organizador ha sido notificado, false en caso contrario.
     */
    public boolean isNotificado() {
        return notificado;
    }

    /**
     * Establece el estado de notificación de la inscripción.
     *
     * @param notificado true si el organizador ha sido notificado, false en caso
     *                   contrario.
     */
    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }
}
