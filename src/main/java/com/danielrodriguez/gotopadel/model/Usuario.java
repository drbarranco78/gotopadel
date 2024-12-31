package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Representa un usuario en el sistema.
 * La entidad está mapeada a la tabla "USUARIO" en la base de datos.
 */
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

    /**
     * Identificador único del usuario.
     * Generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    /**
     * Dirección de correo electrónico del usuario.
     * Es único, obligatorio y tiene una longitud máxima de 50 caracteres.
     */
    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    /**
     * Nombre del usuario.
     * Obligatorio y con una longitud máxima de 70 caracteres.
     */
    @Column(name = "nombre", nullable = false, length = 70)
    private String nombre;

    /**
     * Fecha de nacimiento del usuario en formato de texto.
     */
    @Column(name = "fecha_nac")
    private String fechaNac;

    /**
     * Género del usuario.
     * Opcional y con una longitud máxima de 10 caracteres.
     */
    @Column(name = "genero", length = 10)
    private String genero;

    /**
     * Nivel del usuario, que puede representar su habilidad o categoría.
     * Opcional y con una longitud máxima de 20 caracteres.
     */
    @Column(name = "nivel", length = 20)
    private String nivel;

    /**
     * Fecha en que el usuario se inscribió en el sistema.
     * Representada en formato de texto con una longitud máxima de 10 caracteres.
     */
    @Column(name = "fecha_inscripcion", length = 10)
    private String fechaInscripcion;

    /**
     * Relación uno a uno entre un usuario y sus credenciales.
     * La eliminación de un usuario también elimina sus credenciales.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Credenciales credenciales;

    /**
     * Rol del usuario en el sistema.
     * Define los permisos o el nivel de acceso (e.g., "ADMIN", "USUARIO").
     */
    private String rol;

    // Métodos getter y setter

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return el identificador del usuario
     */
    public Integer getIdUsuario() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id el identificador del usuario
     */
    public void setIdUsuario(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return el correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email el correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre del usuario.
     * 
     * @return el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * 
     * @param nombre el nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha de nacimiento del usuario.
     * 
     * @return la fecha de nacimiento
     */
    public String getFechaNac() {
        return fechaNac;
    }

    /**
     * Establece la fecha de nacimiento del usuario.
     * 
     * @param fechaNac la fecha de nacimiento
     */
    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    /**
     * Obtiene el género del usuario.
     * 
     * @return el género
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Establece el género del usuario.
     * 
     * @param genero el género
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     * Obtiene el nivel del usuario.
     * 
     * @return el nivel
     */
    public String getNivel() {
        return nivel;
    }

    /**
     * Establece el nivel del usuario.
     * 
     * @param nivel el nivel
     */
    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    /**
     * Obtiene la fecha de inscripción del usuario.
     * 
     * @return la fecha de inscripción
     */
    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    /**
     * Establece la fecha de inscripción del usuario.
     * 
     * @param fechaInscripcion la fecha de inscripción
     */
    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    /**
     * Obtiene el rol del usuario.
     * 
     * @return rol del usuario
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     * 
     * @param rol rol del usuario
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}
