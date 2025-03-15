package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.dto.NotificacionDTO;
import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    /**
     * Constructor de la clase NotificacionController.
     * 
     * Este constructor inyecta el servicio necesario para gestionar las notificaciones.
     *
     * @param notificacionService El servicio encargado de gestionar las notificaciones.
     */
    @Autowired
    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    /**
     * Crea una nueva notificación.
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
     * Obtiene las notificaciones de un usuario receptor con datos sobre el emisor y
     * el partido.
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
     * Elimina una notificación por su ID.
     */
    @DeleteMapping("/eliminar/{idNotificacion}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Integer idNotificacion) {
        notificacionService.eliminarNotificacion(idNotificacion);
        return ResponseEntity.noContent().build();
    }

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
