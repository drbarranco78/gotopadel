package com.danielrodriguez.gotopadel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import jakarta.transaction.Transactional;
import java.util.Optional;
import com.danielrodriguez.gotopadel.dto.InscripcionDTO;
import com.danielrodriguez.gotopadel.dto.NotificacionDTO;
import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.InscribeRepository;
import com.danielrodriguez.gotopadel.repository.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private final NotificacionRepository notificacionRepository;
    private final InscribeRepository inscribeRepository;
    private final UsuarioService usuarioService;
    private final PartidoService partidoService;

    @Autowired
    public NotificacionService(InscribeRepository inscribeRepository,
            NotificacionRepository notificacionRepository,
            UsuarioService usuarioService,
            PartidoService partidoService) {
        this.inscribeRepository = inscribeRepository;
        this.notificacionRepository = notificacionRepository;
        this.usuarioService = usuarioService;
        this.partidoService = partidoService;
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
        // Partido partido = partidoService.obtenerPartidoPorId(idPartido)
        //         .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        // Verificar si ya existe una notificación con el mismo emisor, receptor,
        // partido y tipo
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

    public Notificacion obtenerNotificacionCompleta(Integer id) {
        return notificacionRepository.findById(id)
                // .map(this::completarNotificacion)
                .orElse(null);
    }

    /**
     * Actualiza el estado de la notificación para un conjunto de inscripciones
     * que no han sido notificadas aún, según el organizador del partido, y devuelve
     * la lista de usuarios que se han inscrito.
     * 
     * @param organizador El usuario organizador del partido.
     * @return Lista de usuarios que se han inscrito en los partidos del
     *         organizador.
     */
    // public List<NotificacionDTO> notificarInscripciones(Usuario organizador) {
    //     List<Inscribe> inscripciones = inscribeRepository.findByPartido_UsuarioAndNotificadoFalse(organizador);
    //     List<NotificacionDTO> notificacionesDTO = new ArrayList<>();

    //     for (Inscribe inscribe : inscripciones) {
    //         if (!organizador.getIdUsuario().equals(inscribe.getUsuario().getIdUsuario())) {
    //             // Aquí se puede crear una notificación con tipo "inscripcion"
    //             Notificacion notificacion = new Notificacion(
    //                     inscribe.getUsuario(), // Emisor: el usuario que se inscribió
    //                     organizador, // Receptor: el organizador
    //                     inscribe.getPartido(),
    //                     "inscripción");
    //             // Suponiendo que modificamos el constructor o establecemos el tipo
    //             // posteriormente:
    //             // notificacion.setTipo("inscripcion");
    //             notificacionRepository.save(notificacion);

    //             // Convertir a DTO
    //             NotificacionDTO dto = new NotificacionDTO(
    //                     notificacion.getId(),
    //                     inscribe.getUsuario(), // Emisor
    //                     inscribe.getPartido(), // Partido
    //                     notificacion.getTipo(), // Tipo
    //                     notificacion.getFechaCreacion());
    //             notificacionesDTO.add(dto);
    //         }
    //     }
    //     return notificacionesDTO;
    // }

    public void marcarTodasComoLeidas(Usuario usuario) {
        List<Inscribe> inscripciones = inscribeRepository.findByPartido_UsuarioAndNotificadoFalse(usuario);
        for (Inscribe inscribe : inscripciones) {
            inscribe.setNotificado(true);
            inscribeRepository.save(inscribe); // Marca la inscripción como leída
        }
    }
}
