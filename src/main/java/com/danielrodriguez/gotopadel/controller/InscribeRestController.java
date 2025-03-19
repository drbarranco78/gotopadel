package com.danielrodriguez.gotopadel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;
import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.InscribeService;
import com.danielrodriguez.gotopadel.service.PartidoService;

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
   

    /**
     * Constructor de la clase InscribeRestController.
     * 
     * Este constructor se utiliza para inyectar las dependencias necesarias para
     * gestionar las inscripciones, partidos, usuarios y notificaciones a través de los
     * respectivos servicios.
     *
     * @param inscribeService El servicio encargado de gestionar las inscripciones.
     * @param partidoService El servicio encargado de gestionar los partidos.
     */
    @Autowired
    public InscribeRestController(InscribeService inscribeService, PartidoService partidoService) {
        this.inscribeService = inscribeService;
        this.partidoService = partidoService;        
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
    public ResponseEntity<List<Usuario>> obtenerIdsUsuariosInscritos(@PathVariable Integer idPartido) {
        List<Inscribe> inscripciones = inscribeService.obtenerInscripcionesPorPartido(idPartido);
        
        // Extraer los IDs de los usuarios de las inscripciones
        List<Usuario> usuarios = inscripciones.stream()
            .map(inscribe -> inscribe.getUsuario())
            .collect(Collectors.toList());

        return ResponseEntity.ok(usuarios);
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
