package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Usuario;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByReceptor_Id(Integer idReceptor);

    /**
     * Busca una notificación específica en la base de datos basada en el emisor, receptor y mensaje.
     * 
     * @param emisor   El usuario que envió la notificación.
     * @param receptor El usuario que recibió la notificación.
     * @param mensaje  El contenido del mensaje de la notificación.
     * @return Un {@code Optional<Notificacion>} que contiene la notificación si se encuentra, 
     *         o un {@code Optional.empty()} si no existe.
     */
    Optional<Notificacion> findByEmisorAndReceptorAndMensaje(Usuario emisor, Usuario receptor, String mensaje);
}
