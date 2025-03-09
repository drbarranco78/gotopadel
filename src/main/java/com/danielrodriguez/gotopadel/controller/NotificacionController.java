package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.dto.NotificacionDTO;
import com.danielrodriguez.gotopadel.model.Notificacion;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.NotificacionService;
import com.danielrodriguez.gotopadel.service.PartidoService;
import com.danielrodriguez.gotopadel.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioService usuarioService;
    private final PartidoService partidoService;

    @Autowired
    public NotificacionController(NotificacionService notificacionService, UsuarioService usuarioService, PartidoService partidoService) {
        this.notificacionService = notificacionService;
        this.usuarioService = usuarioService;
        this.partidoService = partidoService;
    }

    /**
     * Crea una nueva notificación.
     */
    @PostMapping("/crear")
    public ResponseEntity<Notificacion> crearNotificacion(
            @RequestParam Integer idEmisor,
            @RequestParam Integer idReceptor,
            @RequestParam Integer idPartido,
            @RequestParam String tipo) {

        Notificacion notificacion = notificacionService.crearNotificacion(idEmisor, idReceptor, idPartido, tipo);
        return ResponseEntity.ok(notificacion);
    }

    /**
     * Obtiene las notificaciones de un usuario receptor con datos sobre el emisor y el partido.
     */
    @PostMapping("/usuario/{idReceptor}")
    public ResponseEntity<List<NotificacionDTO>> obtenerNotificaciones(@PathVariable Integer idReceptor) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorReceptor(idReceptor);

        List<NotificacionDTO> notificacionesDTO = notificaciones.stream().map(notif -> 
            new NotificacionDTO(
                notif.getId(),
                notif.getEmisor(),    
                notif.getPartido(),  
                notif.getTipo(),
                notif.getFechaCreacion()
            )
        ).collect(Collectors.toList());

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
}
