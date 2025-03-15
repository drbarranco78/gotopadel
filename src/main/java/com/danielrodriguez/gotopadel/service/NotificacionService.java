package com.danielrodriguez.gotopadel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.InscribeRepository;
import com.danielrodriguez.gotopadel.repository.NotificacionRepository;

/**
 * Servicio encargado de gestionar las notificaciones de la aplicación.
 * Proporciona métodos para eliminar notificaciones, obtener notificaciones completas,
 * y marcar inscripciones como leídas.
 */
@Service
public class NotificacionService {

    @Autowired
    private final NotificacionRepository notificacionRepository;
    private final InscribeRepository inscribeRepository;
    private final UsuarioService usuarioService;

    /**
     * Constructor de la clase NotificacionService.
     *
     * @param inscribeRepository El repositorio de inscripciones que se usará en el servicio.
     * @param notificacionRepository El repositorio de notificaciones que se usará en el servicio.
     * @param usuarioService El servicio de usuario que se usará en el servicio.
     */
    @Autowired
    public NotificacionService(InscribeRepository inscribeRepository,
            NotificacionRepository notificacionRepository,
            UsuarioService usuarioService) {
        this.inscribeRepository = inscribeRepository;
        this.notificacionRepository = notificacionRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Crea y guarda una notificación utilizando los datos de emisor, receptor,
     * idPartido y tipo.
     * Evita duplicados basándose en emisor, receptor, partido y tipo.
     *
     * @param idEmisor   ID del usuario que genera la notificación.
     * @param idReceptor ID del usuario afectado.
     * @param idPartido  ID del partido relacionado.
     * @param tipo       Tipo de notificación ("rechazo", "inscripcion_cancelada",
     *                   "partido_cancelado", etc.)
     * @return La notificación creada o null si ya existe una equivalente.
     */
    public Notificacion crearNotificacion(Integer idEmisor, Integer idReceptor, String mensaje, String tipo ) {
        Usuario emisor = usuarioService.findById(idEmisor)
                .orElseThrow(() -> new RuntimeException("Usuario emisor no encontrado"));
        Usuario receptor = usuarioService.findById(idReceptor)
                .orElseThrow(() -> new RuntimeException("Usuario receptor no encontrado"));
        
        // Verificar si ya existe una notificación con el mismo emisor, receptor, partido y tipo
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
     * Obtiene las notificaciones asociadas a un receptor.
     *
     * @param idReceptor ID del usuario receptor.
     * @return Lista de notificaciones para ese usuario.
     */
    public List<Notificacion> obtenerNotificacionesPorReceptor(Integer idReceptor) {
        return notificacionRepository.findByReceptor_Id(idReceptor);
    }

    /**
     * Elimina una notificación por su ID.
     *
     * @param idNotificacion ID de la notificación a eliminar.
     */
    @Transactional
    public void eliminarNotificacion(Integer idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
    }
    /**
     * Obtiene una notificación completa por su ID.
     *
     * @param id El ID de la notificación que se desea obtener.
     * @return La notificación correspondiente al ID, o null si no se encuentra.
     */
    public Notificacion obtenerNotificacionCompleta(Integer id) {
        return notificacionRepository.findById(id)                
                .orElse(null);
    }

}
