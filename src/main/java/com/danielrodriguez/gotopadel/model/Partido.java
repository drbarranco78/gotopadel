package com.danielrodriguez.gotopadel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

/**
 * Representa un partido de pádel organizado por un usuario.
 * Esta entidad está mapeada a la tabla "PARTIDO" en la base de datos.
 */
@Entity
@Table(name = "PARTIDO")
public class Partido implements Serializable{

    /**
     * Identificador único del partido.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private Integer idPartido;

    /**
     * Usuario que organiza el partido.
     * Es una relación @ManyToOne, ya que un usuario puede organizar varios
     * partidos.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Tipo de partido, individual o dobles
     * Este campo es obligatorio y tiene una longitud máxima de 10 caracteres.
     */
    @Column(name = "tipo_partido", nullable = false, length = 10)
    private String tipoPartido;

    /**
     * Número de vacantes disponibles para el partido.
     * Este campo es obligatorio.
     */
    @Column(name = "vacantes", nullable = false)
    private Byte vacantes;

    /**
     * Nivel de los jugadores requeridos para el partido (por ejemplo, principiante,
     * intermedio, avanzado).
     */
    @Column(name = "nivel", length = 20)
    private String nivel;

    /**
     * Fecha en la que se llevará a cabo el partido.
     * Se almacena como una cadena de texto en formato "DD-MM-YYYY".
     */
    @Column(name = "fecha_partido", length = 10)
    private String fechaPartido;

    /**
     * Fecha en la que se publicó el partido.
     * Se almacena como una cadena de texto en formato "DD-MM-YYYY".
     */
    @Column(name = "fecha_publicacion", length = 10)
    private String fechaPublicacion;

    /**
     * Hora en la que se llevará a cabo el partido.
     * Se almacena como una cadena de texto en formato "HH:mm".
     */
    @Column(name = "hora_partido", length = 6)
    private String horaPartido;

    /**
     * Ubicación donde se llevará a cabo el partido.
     * Es una relación @ManyToOne con la entidad `Ubicacion`.
     */
    @ManyToOne
    @JoinColumn(name = "ubicacion", referencedColumnName = "id_ubicacion")
    private Ubicacion ubicacion;

    /**
     * Comentarios adicionales sobre el partido.
     * Este campo es opcional y puede contener texto de longitud variable.
     */
    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    /**
     * Lista de inscripciones al partido.
     * Es una relación @OneToMany con la entidad `Inscribe`, donde el partido puede
     * tener varias inscripciones.
     * La lista se ignora en la serialización JSON para evitar bucles de
     * referencias.
     */
    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Inscribe> inscripciones = new ArrayList<>();

    // Getters y setters

    /**
     * Constructor vacío requerido para JPA.
     */
    public Partido() {
    }

    /**
     * Obtiene el identificador único del partido.
     *
     * @return el identificador del partido.
     */
    public Integer getIdPartido() {
        return idPartido;
    }

    /**
     * Establece el identificador único del partido.
     *
     * @param idPartido el identificador del partido.
     */
    public void setIdPartido(Integer idPartido) {
        this.idPartido = idPartido;
    }

    /**
     * Obtiene el usuario que organiza el partido.
     *
     * @return el usuario organizador.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que organiza el partido.
     *
     * @param usuario el usuario organizador.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el tipo de partido.
     *
     * @return el tipo de partido.
     */
    public String getTipoPartido() {
        return tipoPartido;
    }

    /**
     * Establece el tipo de partido.
     *
     * @param tipoPartido el tipo de partido.
     */
    public void setTipoPartido(String tipoPartido) {
        this.tipoPartido = tipoPartido;
    }

    /**
     * Obtiene el número de vacantes disponibles.
     *
     * @return el número de vacantes.
     */
    public Byte getVacantes() {
        return vacantes;
    }

    /**
     * Establece el número de vacantes disponibles para el partido.
     *
     * @param vacantes el número de vacantes.
     */
    public void setVacantes(Byte vacantes) {
        this.vacantes = vacantes;
    }

    /**
     * Obtiene el nivel requerido para los jugadores.
     *
     * @return el nivel de los jugadores.
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * Establece el nivel de habilidad requerido para los jugadores.
     *
     * @param nivel el nivel requerido.
     */
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /**
     * Obtiene la fecha del partido.
     *
     * @return la fecha del partido.
     */
    public String getFechaPartido() {
        return fechaPartido;
    }

    /**
     * Establece la fecha programada para el partido.
     *
     * @param fechaPartido la fecha del partido.
     */
    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    /**
     * Obtiene la fecha de publicación del partido.
     *
     * @return la fecha de publicación.
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha en que el partido fue publicado.
     *
     * @param fechaPublicacion la fecha de publicación.
     */
    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene la hora del partido.
     *
     * @return la hora del partido.
     */
    public String getHoraPartido() {
        return horaPartido;
    }

    /**
     * Establece la hora programada para el inicio del partido.
     *
     * @param horaPartido la hora del partido.
     */
    public void setHoraPartido(String horaPartido) {
        this.horaPartido = horaPartido;
    }

    /**
     * Obtiene la ubicación del partido.
     *
     * @return la ubicación del partido.
     */
    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación donde se jugará el partido.
     *
     * @param ubicacion la ubicación del partido.
     */
    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene los comentarios adicionales sobre el partido.
     *
     * @return los comentarios del partido.
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * Establece comentarios adicionales sobre el partido.
     *
     * @param comentarios los comentarios del partido.
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Obtiene la lista de inscripciones al partido.
     *
     * @return la lista de inscripciones.
     */
    public List<Inscribe> getInscripciones() {
        return inscripciones;
    }

    /**
     * Establece la lista de inscripciones asociadas al partido.
     *
     * @param inscripciones la lista de inscripciones.
     */
    public void setInscripciones(List<Inscribe> inscripciones) {
        this.inscripciones = inscripciones;
    }
}
