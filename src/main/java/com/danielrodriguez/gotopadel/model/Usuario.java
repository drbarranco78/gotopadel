package com.danielrodriguez.gotopadel.model;

import jakarta.persistence.*;


@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "nombre", nullable = false, length = 70)
    private String nombre;

    @Column(name = "fecha_nac")
    private String fechaNac;

    @Column(name = "genero", nullable = false)
    private String genero;

    @Column(name = "nivel", length = 20)
    private String nivel;

    @Column(name = "fecha_inscripcion", length = 10)
    private String fechaInscripcion;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Credenciales credenciales;

    public Integer getIdUsuario() {
        return id;
    }

    public void setIdUsuario(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(String fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
}