package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartidoRepository extends JpaRepository<Partido, Integer> {

    @Query("SELECT p FROM Partido p " +
            "LEFT JOIN p.inscripciones i " +
            "WHERE p.usuario.id = :idUsuario OR i.usuario.id = :idUsuario")
    List<Partido> findByUsuarioOrInscripciones_Usuario(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT COUNT(p) FROM Partido p WHERE p.usuario.id = :idUsuario")
    int countPartidosPublicadosPorUsuario(@Param("idUsuario") Integer idUsuario);

}
