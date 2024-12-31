package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las ubicaciones de la aplicación GoToPádel.
 */
@RestController
@RequestMapping("/api/ubicacion")
public class UbicacionRestController {

    @Autowired
    private UbicacionService ubicacionService;

    /**
     * Verifica si una ubicación ya existe en la base de datos o la crea si no existe.
     *
     * @param nombre Nombre de la ubicación.
     * @param ciudad Ciudad de la ubicación.
     * @return La ubicación existente o recién creada.
     */
    @PostMapping("/check")
    public ResponseEntity<Ubicacion> ComprobarOCrearUbicacion(
            @RequestParam String nombre, 
            @RequestParam String ciudad) {
        // Verificar si la ubicación ya existe en la base de datos.
        Ubicacion ubicacion = ubicacionService.findUbicacion(nombre, ciudad);

        if (ubicacion != null) {
            // Si la ubicación ya existe, devolverla.
            return ResponseEntity.ok(ubicacion);
        } else {
            // Si no existe, crear una nueva ubicación y devolverla.
            Ubicacion nuevaUbicacion = ubicacionService.createUbicacion(nombre, ciudad);
            return ResponseEntity.ok(nuevaUbicacion);
        }
    }
}
