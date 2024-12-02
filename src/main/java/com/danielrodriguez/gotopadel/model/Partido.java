package com.danielrodriguez.gotopadel.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "PARTIDO") 
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private Integer idPartido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_partido", nullable = false, length = 10)
    private String tipoPartido;

    @Column(name = "vacantes", nullable = false)
    private Byte vacantes;

    @Column(name = "nivel", length = 20)
    private String nivel;

    @Column(name = "fecha_partido", length = 10)
    private String fechaPartido;

    @Column(name = "fecha_publicacion", length = 10)
    private String fechaPublicacion;

    @Column(name = "hora_partido", length = 6)
    private String horaPartido;

    @ManyToOne
    @JoinColumn(name = "ubicacion", referencedColumnName = "id_ubicacion")
    private Ubicacion ubicacion;

    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Inscribe> inscripciones = new ArrayList<>();

    // Getters y setters
    public Integer getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(Integer idPartido) {
        this.idPartido = idPartido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    public String getTipoPartido() {
        return tipoPartido;
    }

    public void setTipoPartido(String tipoPartido) {
        this.tipoPartido = tipoPartido;
    }

    public Byte getVacantes() {
        return vacantes;
    }

    public void setVacantes(Byte vacantes) {
        this.vacantes = vacantes;
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

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public List<Inscribe> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscribe> inscripciones) {
        this.inscripciones = inscripciones;
    }
}
