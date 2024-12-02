package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UBICACION")
public class Ubicacion {
    public Ubicacion(){

    }

    public Ubicacion(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(name = "nombre", nullable = false, length = 100)  // Asegúrate de que 'nombre' esté correctamente definido
    private String nombre;

    @Column(name = "ciudad", length = 50)
    private String ciudad;

    // Getters y setters
    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}

