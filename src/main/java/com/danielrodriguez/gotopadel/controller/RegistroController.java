package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    @PostMapping
    public ResponseEntity<String> registrarUsuario(@RequestBody Map<String, String> registroDatos) {
        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDatos.get("nombre"));
        nuevoUsuario.setEmail(registroDatos.get("email"));
        nuevoUsuario.setGenero(registroDatos.get("genero"));
        nuevoUsuario.setNivel(registroDatos.get("nivel"));
        nuevoUsuario.setFechaNac(registroDatos.get("fechaNac")); // Añadir fecha de nacimiento
        nuevoUsuario.setFechaInscripcion(registroDatos.get("fechaInscripcion")); // Añadir fecha de inscripción

        boolean registroExitoso = registroService.registrarUsuario(nuevoUsuario, registroDatos.get("password")); // Cambiado a "password"

        if (registroExitoso) {
            return new ResponseEntity<>("Registro exitoso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error en los datos del registro o el email ya está registrado", HttpStatus.BAD_REQUEST);
        }
    }
}
