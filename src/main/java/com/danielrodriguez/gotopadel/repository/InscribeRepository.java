package com.danielrodriguez.gotopadel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.danielrodriguez.gotopadel.model.Inscribe;

/**
 * Repositorio de la entidad Inscribe que extiende JpaRepository.
 * Proporciona métodos personalizados para acceder y manipular los datos de
 * inscripciones en los partidos.
 */
public interface InscribeRepository extends JpaRepository<Inscribe, Integer> {

    /**
     * Verifica si existe una inscripción para un usuario en un partido específico.
     * 
     * @param usuarioId El ID del usuario cuya inscripción se verifica.
     * @param partidoId El ID del partido para el cual se verifica la inscripción.
     * @return true si el usuario está inscrito en el partido, false en caso
     *         contrario.
     */
    boolean existsByUsuario_idAndPartido_idPartido(Integer usuarioId, Integer partidoId);

    /**
     * Busca la inscripción de un usuario a un partido específico utilizando los ID
     * de ambos.
     * 
     * @param usuarioId El ID del usuario cuya inscripción se busca.
     * @param partidoId El ID del partido para el cual se busca la inscripción.
     * @return Un {@link Optional} que contiene la inscripción si existe, o vacío si
     *         no se encuentra.
     */
    Optional<Inscribe> findByUsuario_idAndPartido_idPartido(Integer usuarioId, Integer partidoId);

    /**
     * Cuenta el número de partidos en los que un usuario está inscrito.
     * 
     * @param idUsuario el ID del usuario cuya cantidad de inscripciones se desea contar.
     * @return el número de partidos en los que el usuario está inscrito.
     */
    int countByUsuario_id(int idUsuario);
}
