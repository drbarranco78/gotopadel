package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "INSCRIBE") 

public class Inscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Integer idInscripcion;   

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_partido", nullable = false)
    private Partido partido;

    @Column(name = "fecha_ins")
    private String fechaIns;

    // Constructores, getters y setters

    public Inscribe() {}

    public Inscribe(Usuario usuario, Partido partido, String fechaIns) {
        this.usuario = usuario;
        this.partido = partido;
        this.fechaIns = fechaIns;
    }

    public Integer getIdInscripcion() {
        return idInscripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Partido getPartido() {
        return partido;
    }

    public String getFechaIns() {
        return fechaIns;
    }

    public void setIdInscripcion(Integer idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public void setFechaIns(String fechaIns) {
        this.fechaIns = fechaIns;
    }
    


}
