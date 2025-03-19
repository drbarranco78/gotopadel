package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.dto.NotificacionDTO;
import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de notificaciones en la aplicación.
 * Expone endpoints bajo la ruta base "/api/notificaciones" para interactuar con el servicio de notificaciones.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    /**
     * Constructor que inyecta el servicio de notificaciones.
     *
     * @param notificacionService el servicio encargado de gestionar las notificaciones
     */
    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * Crea una nueva notificación con los datos proporcionados.
     *
     * @param idEmisor el identificador del usuario que envía la notificación
     * @param idReceptor el identificador del usuario que recibe la notificación
     * @param mensaje el contenido de la notificación
     * @param tipo el tipo de notificación (ejemplo: invitación, alerta, etc.)
     * @return una respuesta HTTP con la notificación creada y estado 200 (OK)
     */
    @PostMapping("/crear")
    public ResponseEntity<Notificacion> crearNotificacion(
            @RequestParam Integer idEmisor,
            @RequestParam Integer idReceptor,          
            @RequestParam String mensaje,
            @RequestParam String tipo) {

        Notificacion notificacion = notificacionService.crearNotificacion(idEmisor, idReceptor, mensaje, tipo);
        return ResponseEntity.ok(notificacion);
    }

    /**
     * Obtiene las notificaciones de un usuario receptor en formato DTO, incluyendo datos del emisor y fecha.
     *
     * @param idReceptor el identificador del usuario receptor cuyas notificaciones se desean obtener
     * @return una respuesta HTTP con la lista de notificaciones en formato DTO y estado 200 (OK)
     */
    @PostMapping("/usuario/{idReceptor}")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(@PathVariable Integer idReceptor) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorReceptor(idReceptor);

        List<NotificacionDTO> notificacionesDTO = notificaciones.stream().map(notif -> new NotificacionDTO(
                notif.getId(),
                notif.getEmisor(),                
                notif.getMensaje(),
                notif.getTipo(),                
                notif.getFechaCreacion())).collect(Collectors.toList());

        return ResponseEntity.ok(notificacionesDTO);
    }

    /**
     * Elimina una notificación específica por su identificador.
     *
     * @param idNotificacion el identificador de la notificación a eliminar
     * @return una respuesta HTTP con estado 204 (No Content) si la eliminación es exitosa
     */
    @DeleteMapping("/eliminar/{idNotificacion}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Integer idNotificacion) {
        notificacionService.eliminarNotificacion(idNotificacion);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene una notificación completa por su identificador.
     *
     * @param id el identificador de la notificación a recuperar
     * @return una respuesta HTTP con la notificación y estado 200 (OK) si se encuentra,
     *         o estado 404 (Not Found) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerNotificacionPorID(@PathVariable Integer id) {
        Notificacion notificacion = notificacionService.obtenerNotificacionCompleta(id);
        if (notificacion != null) {
            return ResponseEntity.ok(notificacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}