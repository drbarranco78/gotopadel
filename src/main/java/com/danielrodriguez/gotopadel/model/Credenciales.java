package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "CREDENCIALES")
public class Credenciales implements Serializable {

    @Id
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Integer getIdUsuario() {
        return id;
    }

    public void setIdUsuario(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
