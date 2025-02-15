package com.danielrodriguez.gotopadel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

import com.danielrodriguez.gotopadel.dto.InscripcionDTO;
import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.InscribeService;
import com.danielrodriguez.gotopadel.service.NotificacionService;
import com.danielrodriguez.gotopadel.service.PartidoService;
import com.danielrodriguez.gotopadel.service.UsuarioService;

/**
 * Controlador REST para manejar las inscripciones de usuarios a partidos.
 * Proporciona endpoints para inscribirse, cancelar inscripciones, verificar si
 * un usuario está inscrito y obtener todas las inscripciones.
 */
@RestController
@RequestMapping("/api/inscripciones")
public class InscribeRestController {

    private final InscribeService inscribeService;
    private final PartidoService partidoService;
    private final UsuarioService usuarioService;
    private final NotificacionService notificacionService;

    @Autowired
    public InscribeRestController(InscribeService inscribeService, PartidoService partidoService,
            UsuarioService usuarioService, NotificacionService notificacionService) {
        this.inscribeService = inscribeService;
        this.partidoService = partidoService;
        this.usuarioService = usuarioService;
        this.notificacionService = notificacionService;
    }

    /**
     * Endpoint para inscribir a un usuario en un partido.
     *
     * @param inscribe Objeto Inscribe que contiene los datos de la inscripción.
     * @return La inscripción recién creada.
     */
    @PostMapping
    public Inscribe createInscription(@RequestBody Inscribe inscribe) {
        Inscribe nuevaInscripcion = inscribeService.inscribir(inscribe);
        partidoService.descontarVacante(nuevaInscripcion.getPartido().getIdPartido()); // Descuenta una vacante
        return nuevaInscripcion;
    }

    /**
     * Notifica las inscripciones no notificadas de un organizador de partido y
     * devuelve la lista de usuarios que se han inscrito.
     * 
     * @param organizadorId El ID del usuario organizador del partido.
     * @return Lista de usuarios inscritos en los partidos del organizador.
     */
    @PostMapping("/notificar/{organizadorId}")
    public ResponseEntity<?> notificarInscripciones(@PathVariable Integer organizadorId) {
        // Buscar el usuario en la base de datos
        Usuario organizador = usuarioService.findById(organizadorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            List<InscripcionDTO> inscripciones = notificacionService.notificarInscripciones(organizador);
            return ResponseEntity.ok(inscripciones); // Devuelve la lista en JSON
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al notificar inscripciones: " + e.getMessage());
        }
    }

    @PostMapping("/marcarComoLeidas/{organizadorId}")
    public ResponseEntity<?> marcarNotificacionesComoLeidas(@PathVariable Integer organizadorId) {
        Usuario organizador = usuarioService.findById(organizadorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            // Marcar todas las inscripciones del organizador como leídas (notificadas)
            notificacionService.marcarTodasComoLeidas(organizador);
            return ResponseEntity.ok("Notificaciones marcadas como leídas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al marcar notificaciones como leídas: " + e.getMessage());
        }
    }

    /**
     * Endpoint para cancelar una inscripción de un usuario en un partido.
     *
     * @param idUsuario ID del usuario que desea cancelar la inscripción.
     * @param idPartido ID del partido en el que está inscrito el usuario.
     * @return Un mensaje indicando si la cancelación fue exitosa o si hubo un
     *         problema.
     */
    @DeleteMapping("/cancelar")
    public ResponseEntity<?> cancelarInscripcion(
            @RequestParam Integer idUsuario, @RequestParam Integer idPartido) {
        boolean existeInscripcion = inscribeService.verificarInscripcion(idUsuario, idPartido);

        if (!existeInscripcion) {
            return ResponseEntity.status(404).body("No se encontró inscripción para el usuario en este partido.");
        }

        // Cancelar la inscripción y devolver la vacante al partido
        inscribeService.cancelarInscripcionPorUsuarioYPartido(idUsuario, idPartido);
        partidoService.aumentarVacante(idPartido);

        return ResponseEntity.ok("Inscripción cancelada exitosamente.");
    }

    /**
     * Endpoint para obtener todas las inscripciones.
     *
     * @return Una lista de inscripciones existentes.
     */
    @GetMapping
    public List<Inscribe> getAllInscriptions() {
        return inscribeService.obtenerListaInscritos();
    }

    /**
     * Endpoint para verificar si un usuario está inscrito en un partido.
     *
     * @param idUsuario ID del usuario que se quiere verificar.
     * @param idPartido ID del partido en el que se quiere verificar la inscripción.
     * @return Un mapa que contiene el estado de la inscripción: `true` si está
     *         inscrito, `false` si no lo está.
     */
    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Boolean>> verificarInscripcion(@RequestParam Integer idUsuario,
            @RequestParam Integer idPartido) {
        boolean inscrito = inscribeService.verificarInscripcion(idUsuario, idPartido);
        Map<String, Boolean> response = new HashMap<>();
        response.put("inscrito", inscrito);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la cantidad de partidos en los que un usuario está inscrito.
     *
     * @param idUsuario el ID del usuario.
     * @return el número de partidos en los que está inscrito.
     */
    @GetMapping("/cantidad/{idUsuario}")
    public int obtenerCantidadInscripciones(@PathVariable int idUsuario) {
        return inscribeService.obtenerCantidadInscripciones(idUsuario);
    }
}
