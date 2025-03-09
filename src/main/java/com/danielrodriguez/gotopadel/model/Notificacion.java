package com.danielrodriguez.gotopadel.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;

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

    // @ManyToOne
    // @JoinColumn(name = "id_partido", nullable = false)
    // private Partido partido;

    @Column(nullable = true)
    private String tipo;

    @Column(nullable = true, columnDefinition = "TEXT") 
    private String mensaje;

    

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public Notificacion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Notificacion(Usuario emisor, Usuario receptor, String mensaje, String tipo) {
        this.emisor = emisor;
        this.receptor = receptor;
        //this.partido = partido;
        this.mensaje = mensaje;
        this.tipo = tipo;
        
        this.fechaCreacion = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    // public Partido getPartido() {
    //     return partido;
    // }

    // public void setPartido(Partido partido) {
    //     this.partido = partido;
    // }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    
}