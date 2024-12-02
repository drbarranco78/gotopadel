package com.danielrodriguez.gotopadel.controller;

import java.util.List;

//
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


import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.service.InscribeService;
import com.danielrodriguez.gotopadel.service.PartidoService;

@RestController
@RequestMapping("/api/inscripciones")

public class InscribeRestController {

    private final InscribeService inscribeService;
    private final PartidoService partidoService;

    @Autowired
    public InscribeRestController(InscribeService inscribeService, PartidoService partidoService) {
        this.inscribeService = inscribeService;
        this.partidoService = partidoService;
    }

    @PostMapping
    public Inscribe createInscription(@RequestBody Inscribe inscribe) {
        Inscribe nuevaInscripcion = inscribeService.inscribir(inscribe);
        partidoService.descontarVacante(nuevaInscripcion.getPartido().getIdPartido()); // Descuenta una vacante
        return nuevaInscripcion;
    }

    @DeleteMapping("/cancelar")
    public ResponseEntity<?> cancelarInscripcion(
            @RequestParam Integer idUsuario, @RequestParam Integer idPartido) {
    
        boolean existeInscripcion = inscribeService.verificarInscripcion(idUsuario, idPartido);
    
        if (!existeInscripcion) {
            return ResponseEntity.status(404).body("No se encontró inscripción para el usuario en este partido.");
        }
    
        // Aquí podrías obtener el ID de inscripción para borrarlo, si fuera necesario
        inscribeService.cancelarInscripcionPorUsuarioYPartido(idUsuario, idPartido);
        partidoService.aumentarVacante(idPartido); // Devuelve la vacante al partido
    
        return ResponseEntity.ok("Inscripción cancelada exitosamente.");
    }
    

    @GetMapping
    public List<Inscribe> getAllInscriptions() {
        return inscribeService.obtenerListaInscritos();
    }

    @GetMapping("/verificar")
    public ResponseEntity<Map<String, Boolean>> verificarInscripcion(@RequestParam Integer idUsuario,
            @RequestParam Integer idPartido) {
        boolean inscrito = inscribeService.verificarInscripcion(idUsuario, idPartido);
        Map<String, Boolean> response = new HashMap<>();
        response.put("inscrito", inscrito);
        return ResponseEntity.ok(response);
    }

}
