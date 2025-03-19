package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Usuario;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de notificaciones en la base de datos.
 * Proporciona métodos para acceder y manipular datos de la entidad {@link Notificacion}.
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    /**
     * Busca todas las notificaciones asociadas a un receptor específico por su identificador.
     *
     * @param idReceptor el identificador del usuario receptor
     * @return una lista de notificaciones asociadas al receptor
     */
    List<Notificacion> findByReceptor_Id(Integer idReceptor);

    /**
     * Busca una notificación específica en la base de datos basada en el emisor, receptor y mensaje.
     *
     * @param emisor el usuario que envió la notificación
     * @param receptor el usuario que recibió la notificación
     * @param mensaje el contenido del mensaje de la notificación
     * @return un {@code Optional<Notificacion>} que contiene la notificación si se encuentra,
     *         o {@code Optional.empty()} si no existe
     */
    Optional<Notificacion> findByEmisorAndReceptorAndMensaje(Usuario emisor, Usuario receptor, String mensaje);
}