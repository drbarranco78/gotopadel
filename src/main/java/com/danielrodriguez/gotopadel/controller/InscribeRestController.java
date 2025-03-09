package com.danielrodriguez.gotopadel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Obtiene los IDs de los usuarios inscritos a un partido específico.
     *
     * @param idPartido ID del partido.
     * @return Lista de IDs de usuarios inscritos.
     */
    @GetMapping("/partido/{idPartido}/usuarios")
    public ResponseEntity<List<Integer>> obtenerIdsUsuariosInscritos(@PathVariable Integer idPartido) {
        List<Inscribe> inscripciones = inscribeService.obtenerInscripcionesPorPartido(idPartido);
        
        // Extraer los IDs de los usuarios de las inscripciones
        List<Integer> idsUsuarios = inscripciones.stream()
            .map(inscribe -> inscribe.getUsuario().getIdUsuario())
            .collect(Collectors.toList());

        return ResponseEntity.ok(idsUsuarios);
    }

    
    // @PostMapping("/notificar-inscripciones/{organizadorId}")
    // public ResponseEntity<?> notificarInscripciones(@PathVariable Integer organizadorId) {
    //     // Buscar el usuario en la base de datos
    //     Usuario organizador = usuarioService.findById(organizadorId)
    //             .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    //     try {
    //         List<InscripcionDTO> inscripciones = notificacionService.notificarInscripciones(organizador);
    //         return ResponseEntity.ok(inscripciones); // Devuelve la lista en JSON
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body("Error al notificar inscripciones: " + e.getMessage());
    //     }
    // }

    // @PostMapping("/notificar-cancelaciones/{jugadorId}")
    // public ResponseEntity<?> notificarCancelaciones(@PathVariable Integer jugadorId) {
    //     Usuario jugador = usuarioService.findById(jugadorId)
    //             .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    //     return null;

    // }

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
 

    @PutMapping("/modificarEstado")
    public ResponseEntity<String> modificarEstadoInscripcion(@RequestBody InscripcionDTO inscripcionDTO) {
        try {
            // Extraer los IDs del usuario y el partido
            Integer idUsuario = inscripcionDTO.getUsuario().getIdUsuario();
            Integer idPartido = inscripcionDTO.getPartido().getIdPartido();
            String estado = inscripcionDTO.getEstado(); 

            // Llamar al servicio con los parámetros extraídos
            boolean resultado = inscribeService.modificarEstadoInscripcion(idUsuario, idPartido, estado);

            if (resultado) {
                if (estado == "cancelada" || estado == "rechazada") {
                    partidoService.aumentarVacante(idPartido);
                }
                return ResponseEntity.ok("Estado de la inscripción actualizado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se pudo actualizar el estado de la inscripción");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }

}
