package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Representa las credenciales de un usuario en el sistema, incluyendo su contraseña.
 * La entidad está vinculada a la tabla "CREDENCIALES" en la base de datos.
 * Esta clase mapea la relación de un usuario con sus credenciales de acceso.
 */
@Entity
@Table(name = "CREDENCIALES")
public class Credenciales implements Serializable {

    /**
     * Identificador único de la credencial, que también se utiliza como clave foránea al usuario.
     */
    @Id
    @Column(name = "id_usuario")
    private Integer id;

    /**
     * Contraseña del usuario al que está asociada la credencial.
     */
    @Column(name = "password", nullable = false, length = 20)
    private String password;

    /**
     * Relación uno a uno entre las credenciales y el usuario al que pertenecen.
     */
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    /**
     * Obtiene el identificador de usuario asociado con estas credenciales.
     * 
     * @return el identificador del usuario
     */
    public Integer getIdUsuario() {
        return id;
    }

    /**
     * Establece el identificador de usuario para estas credenciales.
     * 
     * @param id el identificador del usuario
     */
    public void setIdUsuario(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return la contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param password la nueva contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el usuario asociado con estas credenciales.
     * 
     * @return el usuario asociado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario asociado con estas credenciales.
     * 
     * @param usuario el usuario al que asociar estas credenciales
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
