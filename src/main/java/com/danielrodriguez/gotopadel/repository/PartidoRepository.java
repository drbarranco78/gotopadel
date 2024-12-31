package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Partido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de la entidad Partido que extiende JpaRepository.
 * Proporciona métodos personalizados para acceder y manipular los datos de partidos en la base de datos.
 */
public interface PartidoRepository extends JpaRepository<Partido, Integer> {

    /**
     * Consulta para obtener la lista de partidos en los que un usuario está involucrado, ya sea como organizador o inscrito.
     * Se hace una unión con la tabla de inscripciones para obtener partidos en los que el usuario esté inscrito,
     * además de los partidos que haya creado el usuario.
     *
     * @param idUsuario El ID del usuario cuya participación en partidos se va a consultar.
     * @return Una lista de partidos en los que el usuario está involucrado.
     */
    @Query("SELECT p FROM Partido p " +
            "LEFT JOIN p.inscripciones i " +
            "WHERE p.usuario.id = :idUsuario OR i.usuario.id = :idUsuario")
    List<Partido> findByUsuarioOrInscripciones_Usuario(@Param("idUsuario") Integer idUsuario);

    /**
     * Consulta para contar cuántos partidos ha publicado un usuario.
     *
     * @param idUsuario El ID del usuario cuyo número de partidos publicados se va a contar.
     * @return El número de partidos publicados por el usuario.
     */
    @Query("SELECT COUNT(p) FROM Partido p WHERE p.usuario.id = :idUsuario")
    int countPartidosPublicadosPorUsuario(@Param("idUsuario") Integer idUsuario);

}

