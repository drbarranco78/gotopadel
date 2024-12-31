package com.danielrodriguez.gotopadel.model;

import java.io.Serializable;

import jakarta.persistence.*;

/**
 * Representa un partido archivado en la aplicación.
 * Contiene información del partido y el motivo de su archivado.
 */
@Entity
@Table(name = "ARCHIVO") 
public class Archivo implements Serializable{

    /**
     * Identificador único del partido.
     */
    @Id
    @Column(name = "id_partido", nullable = false)
    private Integer idPartido;

    /**
     * Identificador del usuario que organizó el partido.
     */
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    /**
     * Tipo del partido.
     */
    @Column(name = "tipo_partido", nullable = false, length = 10)
    private String tipoPartido;

    /**
     * Nivel requerido para los jugadores.
     */
    @Column(name = "nivel", length = 20)
    private String nivel;

    /**
     * Fecha programada para el partido.
     */
    @Column(name = "fecha_partido", length = 10)
    private String fechaPartido;

    /**
     * Fecha en que el partido fue publicado.
     */
    @Column(name = "fecha_publicacion", length = 10)
    private String fechaPublicacion;

    /**
     * Hora programada para el partido.
     */
    @Column(name = "hora_partido", length = 6)
    private String horaPartido;

    /**
     * Ubicación del partido representada por su identificador.
     */
    @Column
    private Integer ubicacion;

    /**
     * Comentarios adicionales sobre el partido.
     */
    @Column(columnDefinition = "TEXT")
    private String comentarios;

    /**
     * Fecha en que el partido fue archivado.
     */
    @Column(name = "fecha_archivo", length = 10)
    private String fechaArchivo;

    /**
     * Motivo del archivado del partido.
     */
    @Column(name = "motivo_archivado", length = 30)
    private String motivoArchivado;

    // Getters y setters

    /**
     * Obtiene el identificador del partido.
     *
     * @return el identificador del partido.
     */
    public Integer getIdPartido() {
        return idPartido;
    }

    /**
     * Establece el identificador del partido.
     *
     * @param idPartido el identificador del partido.
     */
    public void setIdPartido(Integer idPartido) {
        this.idPartido = idPartido;
    }

    /**
     * Obtiene el identificador del usuario que organizó el partido.
     *
     * @return el identificador del usuario.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del usuario que organizó el partido.
     *
     * @param idUsuario el identificador del usuario.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el tipo del partido.
     *
     * @return el tipo del partido.
     */
    public String getTipoPartido() {
        return tipoPartido;
    }

    /**
     * Establece el tipo del partido.
     *
     * @param tipoPartido el tipo del partido.
     */
    public void setTipoPartido(String tipoPartido) {
        this.tipoPartido = tipoPartido;
    }

    /**
     * Obtiene el nivel de juego del partido.
     *
     * @return el nivel requerido.
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * Establece el nivel de juego del partido.
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
     * Establece la fecha del partido.
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
     * Establece la fecha de publicación del partido.
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
     * Establece la hora del partido.
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
    public Integer getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación del partido.
     *
     * @param ubicacion la ubicación del partido.
     */
    public void setUbicacion(Integer ubicacion) {
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
     * Establece los comentarios adicionales sobre el partido.
     *
     * @param comentarios los comentarios del partido.
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * Obtiene la fecha en que el partido fue archivado.
     *
     * @return la fecha de archivado.
     */
    public String getFechaArchivo() {
        return fechaArchivo;
    }

    /**
     * Establece la fecha en que el partido fue archivado.
     *
     * @param fechaArchivo la fecha de archivado.
     */
    public void setFechaArchivo(String fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    /**
     * Obtiene el motivo del archivado del partido.
     *
     * @return el motivo del archivado.
     */
    public String getMotivoArchivado() {
        return motivoArchivado;
    }

    /**
     * Establece el motivo del archivado del partido.
     *
     * @param motivoArchivado el motivo del archivado.
     */
    public void setMotivoArchivado(String motivoArchivado) {
        this.motivoArchivado = motivoArchivado;
    }
}
