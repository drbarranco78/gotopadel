package com.danielrodriguez.gotopadel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/privado")
public class ZonaPrivadaController {

    @GetMapping("/datosUsuario")
    public ResponseEntity<Map<String, String>> obtenerDatosUsuario(HttpSession session) {
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");

        if (nombreUsuario != null && idUsuario != null) {
            Map<String, String> datosUsuario = new HashMap<>();
            datosUsuario.put("idUsuario", idUsuario.toString());
            datosUsuario.put("nombreUsuario", nombreUsuario);
            return new ResponseEntity<>(datosUsuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
