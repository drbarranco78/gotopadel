package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partido")
public class PartidoRestController {
    private final PartidoService partidoService;

    @Autowired
    public PartidoRestController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    // Endpoint para obtener todos los partidos
    @GetMapping
    public List<Partido> obtenerTodosLosPartidos() {
        return partidoService.obtenerTodosLosPartidos();
    }

    // Endpoint para obtener un partido por id
    @GetMapping("/{id}")
    public ResponseEntity<Partido> obtenerPartidoPorId(@PathVariable int id) {
        Optional<Partido> partido = partidoService.obtenerPartidoPorId(id);
        return partido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para obtener los partidos por usuario
    @GetMapping("/misPartidos")
    public List<Partido> obtenerMisPartidos(@RequestParam Integer usuario) {
        return partidoService.obtenerPartidosPorUsuario(usuario);
    }

    // Endpoint para crear un nuevo partido
    @PostMapping
    public Partido crearPartido(@RequestBody Partido partido) {
        return partidoService.guardarPartido(partido);
    }

    // Endpoint para eliminar un partido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPartido(@PathVariable int id) {
        partidoService.eliminarPartido(id);
        return ResponseEntity.noContent().build();
    }
}
