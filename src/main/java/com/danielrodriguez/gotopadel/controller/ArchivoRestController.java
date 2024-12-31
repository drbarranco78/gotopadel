package com.danielrodriguez.gotopadel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.danielrodriguez.gotopadel.model.Archivo;
import com.danielrodriguez.gotopadel.service.ArchivoService;

/**
 * Controlador REST para manejar las operaciones relacionadas con el archivado
 * de partidos.
 * Proporciona endpoints para archivar partidos, obtener partidos archivados y
 * eliminarlos.
 */
@RestController
@RequestMapping("/api/archivo")
public class ArchivoRestController {

    private final ArchivoService archivoService;

    @Autowired
    public ArchivoRestController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    /**
     * Endpoint para archivar un partido.
     *
     * @param idPartido       ID del partido que se quiere archivar.
     * @param motivoArchivado Motivo del archivado: 1 para "Partido Jugado", cualquier otro valor para "Partido Cancelado".
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @PostMapping
    public ResponseEntity<String> archivarPartido(@RequestParam Integer idPartido,
            @RequestParam Integer motivoArchivado) {
        System.out.println("ID Partido: " + idPartido);
        System.out.println("Motivo Archivado: " + motivoArchivado);

        try {
            archivoService.archivarPartido(idPartido, motivoArchivado);
            return ResponseEntity.ok("Partido archivado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error al archivar el partido: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todos los partidos archivados.
     *
     * @return Una lista de objetos Archivo que representa los partidos archivados.
     */
    @GetMapping("/obtenerPartidosArchivados")
    public List<Archivo> obtenerPartidosArchivados() {
        return archivoService.obtenerPartidosArchivados();
    }

    /**
     * Endpoint para eliminar un partido archivado por su ID.
     *
     * @param id ID del partido archivado a eliminar.
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @DeleteMapping("/eliminarPartidoArchivado/{id}")
    public ResponseEntity<String> eliminarPartidoArchivado(@PathVariable Integer id) {
        try {
            archivoService.eliminarPartidoArchivadoPorId(id);
            return new ResponseEntity<>("Partido archivado eliminado con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el partido archivado", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
