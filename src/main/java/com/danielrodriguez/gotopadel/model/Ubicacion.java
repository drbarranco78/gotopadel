package com.danielrodriguez.gotopadel.model;

import java.io.Serializable;

import jakarta.persistence.*;

/**
 * Representa una ubicación en el sistema, como una instalación deportiva y su ciudad.
 * Esta entidad está mapeada a la tabla "UBICACION" en la base de datos.
 */
@Entity
@Table(name = "UBICACION")
public class Ubicacion implements Serializable {

    /**
     * Constructor vacío requerido para JPA.
     */
    public Ubicacion() {
    }

    /**
     * Constructor que permite inicializar una ubicación con nombre y ciudad.
     *
     * @param nombre Nombre de la ubicación.
     * @param ciudad Ciudad donde se encuentra la ubicación.
     */
    public Ubicacion(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    /**
     * Identificador único de la ubicación.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    /**
     * Nombre de la ubicación.
     * Obligatorio y con una longitud máxima de 100 caracteres.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Ciudad donde se encuentra la ubicación.
     * Opcional y con una longitud máxima de 50 caracteres.
     */
    @Column(name = "ciudad", length = 50)
    private String ciudad;

    // Getters y setters

    /**
     * Obtiene el identificador único de la ubicación.
     *
     * @return el identificador de la ubicación.
     */
    public Integer getIdUbicacion() {
        return idUbicacion;
    }

    /**
     * Establece el identificador único de la ubicación.
     *
     * @param idUbicacion el identificador de la ubicación.
     */
    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    /**
     * Obtiene el nombre de la ubicación.
     *
     * @return el nombre de la ubicación.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la ubicación.
     *
     * @param nombre el nombre de la ubicación.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ciudad donde se encuentra la ubicación.
     *
     * @return la ciudad de la ubicación.
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde se encuentra la ubicación.
     *
     * @param ciudad la ciudad de la ubicación.
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
