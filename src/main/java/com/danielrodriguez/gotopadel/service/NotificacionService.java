package com.danielrodriguez.gotopadel.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.NotificacionRepository;

/**
 * Servicio encargado de gestionar las notificaciones de la aplicación.
 * Proporciona métodos para crear, eliminar y obtener notificaciones, 
 * así como para verificar duplicados y manejar transacciones.
 */
@Service
public class NotificacionService {

    @Autowired
    private final NotificacionRepository notificacionRepository;
    private final UsuarioService usuarioService;

    /**
     * Constructor que inyecta los repositorios y servicios necesarios para la gestión de notificaciones.
     *
     * @param notificacionRepository el repositorio para acceder y manipular datos de notificaciones
     * @param usuarioService el servicio para gestionar datos de usuarios
     */
    @Autowired
    public NotificacionService(
            NotificacionRepository notificacionRepository,
            UsuarioService usuarioService) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Crea y guarda una notificación utilizando los datos proporcionados, evitando duplicados.
     * Verifica si ya existe una notificación con el mismo emisor, receptor y mensaje antes de crearla.
     *
     * @param idEmisor el identificador del usuario que genera la notificación
     * @param idReceptor el identificador del usuario que recibe la notificación
     * @param mensaje el contenido de la notificación
     * @param tipo el tipo de notificación (ejemplo: "rechazo", "inscripcion_cancelada", "partido_cancelado")
     * @return la notificación creada, o {@code null} si ya existe una notificación equivalente
     * @throws RuntimeException si el emisor o receptor no se encuentran en la base de datos
     */
    public Notificacion crearNotificacion(Integer idEmisor, Integer idReceptor, String mensaje, String tipo) {
        Usuario emisor = usuarioService.findById(idEmisor)
                .orElseThrow(() -> new RuntimeException("Usuario emisor no encontrado"));
        Usuario receptor = usuarioService.findById(idReceptor)
                .orElseThrow(() -> new RuntimeException("Usuario receptor no encontrado"));

        // Verificar si ya existe una notificación con el mismo emisor, receptor y mensaje
        Optional<Notificacion> notificacionExistente = notificacionRepository
                .findByEmisorAndReceptorAndMensaje(emisor, receptor, mensaje);

        if (notificacionExistente.isPresent()) {
            // Si ya existe una notificación idéntica, no creamos una nueva
            return null;
        }

        // Si no existe, crear una nueva notificación
        Notificacion notificacion = new Notificacion(emisor, receptor, mensaje, tipo);
        return notificacionRepository.save(notificacion);
    }

    /**
     * Obtiene todas las notificaciones asociadas a un receptor específico.
     *
     * @param idReceptor el identificador del usuario receptor
     * @return una lista de notificaciones asociadas al receptor
     */
    public List<Notificacion> obtenerNotificacionesPorReceptor(Integer idReceptor) {
        return notificacionRepository.findByReceptor_Id(idReceptor);
    }

    /**
     * Elimina una notificación específica por su identificador.
     * Este método está anotado con {@code @Transactional} para garantizar la integridad de la operación.
     *
     * @param idNotificacion el identificador de la notificación a eliminar
     */
    @Transactional
    public void eliminarNotificacion(Integer idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
    }

    /**
     * Obtiene una notificación completa por su identificador.
     *
     * @param id el identificador de la notificación a recuperar
     * @return la notificación correspondiente al ID, o {@code null} si no se encuentra
     */
    public Notificacion obtenerNotificacionCompleta(Integer id) {
        return notificacionRepository.findById(id)
                .orElse(null);
    }
}