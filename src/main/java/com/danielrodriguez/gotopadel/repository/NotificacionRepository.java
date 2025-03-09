package com.danielrodriguez.gotopadel.repository;

import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByReceptor_Id(Integer idReceptor);

    // Optional<Notificacion> findByEmisorAndReceptorAndPartidoAndTipo(Usuario emisor, Usuario receptor, Partido partido, String tipo);
    Optional<Notificacion> findByEmisorAndReceptorAndMensaje(Usuario emisor, Usuario receptor, String mensaje);
}
