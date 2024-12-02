package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.service.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ubicacion")
public class UbicacionRestController {

    @Autowired
    private UbicacionService ubicacionService;

    @PostMapping("/check")
    public ResponseEntity<Ubicacion> checkOrCreateUbicacion(@RequestParam String nombre, @RequestParam String ciudad) {
        // Verificar si la ubicación ya existe en la BD
        Ubicacion ubicacion = ubicacionService.findUbicacion(nombre, ciudad);

        if (ubicacion != null) {
            // Si existe, devolverla
            return ResponseEntity.ok(ubicacion);
        } else {
            // Si no existe, crearla y devolverla
            Ubicacion nuevaUbicacion = ubicacionService.createUbicacion(nombre, ciudad);
            return ResponseEntity.ok(nuevaUbicacion);
        }
    }
}
