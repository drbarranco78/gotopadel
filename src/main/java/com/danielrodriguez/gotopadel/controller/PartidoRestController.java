package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los partidos de pádel.
 * Proporciona endpoints para realizar operaciones CRUD sobre los partidos.
 */
@RestController
@RequestMapping("/api/partido")
public class PartidoRestController {

    private final PartidoService partidoService;

    /**
     * Constructor para inyectar el servicio de partidos.
     *
     * @param partidoService Servicio utilizado para las operaciones relacionadas con partidos.
     */
    @Autowired
    public PartidoRestController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    /**
     * Endpoint para obtener todos los partidos.
     *
     * @return Una lista con todos los partidos disponibles.
     */
    @GetMapping
    public List<Partido> obtenerTodosLosPartidos() {
        return partidoService.obtenerTodosLosPartidos();
    }

    /**
     * Endpoint para obtener un partido por su ID.
     *
     * @param id ID del partido que se desea obtener.
     * @return El partido correspondiente al ID si existe, o un código 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Partido> obtenerPartidoPorId(@PathVariable int id) {
        Optional<Partido> partido = partidoService.obtenerPartidoPorId(id);
        return partido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para obtener los partidos asociados a un usuario.
     *
     * @param usuario ID del usuario cuyos partidos se desean obtener.
     * @return Una lista de partidos asociados al usuario.
     */
    @GetMapping("/misPartidos")
    public List<Partido> obtenerMisPartidos(@RequestParam Integer usuario) {
        return partidoService.obtenerPartidosPorUsuario(usuario);
    }

    /**
     * Endpoint para crear un nuevo partido.
     *
     * @param partido Objeto Partido con la información del nuevo partido.
     * @return El partido creado con su ID asignado.
     */
    @PostMapping
    public Partido crearPartido(@RequestBody Partido partido) {
        return partidoService.guardarPartido(partido);
    }

    /**
     * Endpoint para eliminar un partido por su ID.
     *
     * @param id ID del partido que se desea eliminar.
     * @return Una respuesta sin contenido (204) si la operación es exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPartido(@PathVariable int id) {
        partidoService.eliminarPartido(id);
        return ResponseEntity.noContent().build();
    }
}
