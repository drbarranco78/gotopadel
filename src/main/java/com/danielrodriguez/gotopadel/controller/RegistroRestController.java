package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.RegistroService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping
    public ResponseEntity<Map<String,String>> registrarUsuario(@RequestBody Map<String, String> registroDatos, HttpSession sesion) {
        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDatos.get("nombre"));
        nuevoUsuario.setEmail(registroDatos.get("email"));
        nuevoUsuario.setGenero(registroDatos.get("genero"));
        nuevoUsuario.setNivel(registroDatos.get("nivel"));
        nuevoUsuario.setFechaNac(registroDatos.get("fechaNac")); // Añadir fecha de nacimiento
        nuevoUsuario.setFechaInscripcion(registroDatos.get("fechaInscripcion")); // Añadir fecha de inscripción

        if (registroService.emailExiste(registroDatos.get("email"))) {
            // Devolver un código 409 Conflict si el email ya está registrado
            Map<String, String> response = new HashMap<>();
            response.put("error", "El email ya está registrado");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        boolean registroExitoso = registroService.registrarUsuario(nuevoUsuario, registroDatos.get("password")); // Cambiado a "password"

        if (registroExitoso) {
            sesion.setAttribute("idUsuario", nuevoUsuario.getIdUsuario());
            sesion.setAttribute("nombreUsuario", nuevoUsuario.getNombre());
            // Crear la respuesta con el nombre y el ID
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("nombreUsuario", nuevoUsuario.getNombre());
            respuesta.put("idUsuario", nuevoUsuario.getIdUsuario().toString());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
