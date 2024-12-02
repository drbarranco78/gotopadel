package com.danielrodriguez.gotopadel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {
    @Autowired
    private UsuarioService usuarioService;

    // Login del usuario 
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginDatos, HttpSession session) {
        String email = loginDatos.get("email");
        String password = loginDatos.get("password");

        Usuario usuario = usuarioService.login(email, password);

        if (usuario != null) {
            // Si el login es exitoso, guardar datos en la sesión         
            session.setAttribute("usuarioAutenticado" , usuario);
            if (email.equals("admin@email.com")) {
                usuario.setRol("ADMIN");
                return new ResponseEntity<>("Login exitoso, redirigiendo a administrador.html", HttpStatus.OK);
            }
            return new ResponseEntity<>("Login exitoso, redirigiendo a zonaPersonal.html", HttpStatus.OK);
        } else {
            // Si las credenciales son incorrectas
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión del usuario
        return new ResponseEntity<>("Sesión cerrada correctamente", HttpStatus.OK);
    }

    // Registro del usuario 

    @PostMapping("/registrarUsuario")
    public ResponseEntity<Map<String,String>> registrarUsuario(@RequestBody Map<String, String> registroDatos, HttpSession sesion) {
        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDatos.get("nombre"));
        nuevoUsuario.setEmail(registroDatos.get("email"));
        nuevoUsuario.setGenero(registroDatos.get("genero"));
        nuevoUsuario.setNivel(registroDatos.get("nivel"));
        nuevoUsuario.setFechaNac(registroDatos.get("fechaNac")); // Añadir fecha de nacimiento
        nuevoUsuario.setFechaInscripcion(registroDatos.get("fechaInscripcion")); // Añadir fecha de inscripción
        

        if (usuarioService.emailExiste(registroDatos.get("email"))) {
            // Devolver un código 409 Conflict si el email ya está registrado
            Map<String, String> response = new HashMap<>();
            response.put("error", "El email ya está registrado");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        boolean registroExitoso = usuarioService.registrarUsuario(nuevoUsuario, registroDatos.get("password")); // Cambiado a "password"

        if (registroExitoso) {
            sesion.setAttribute("usuarioAutenticado" , nuevoUsuario);
            //sesion.setAttribute("idUsuario", nuevoUsuario.getIdUsuario());
            //sesion.setAttribute("nombreUsuario", nuevoUsuario.getNombre());
            // Crear la respuesta con el nombre y el ID
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("nombreUsuario", nuevoUsuario.getNombre());
            respuesta.put("idUsuario", nuevoUsuario.getIdUsuario().toString());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/datosUsuario")
    public ResponseEntity<Usuario> obtenerDatosUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado"); // Guarda el objeto Usuario completo

        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // Endpoint para eliminar la cuenta de usuario
    @DeleteMapping("/eliminarCuenta/{idUsuario}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable Integer idUsuario, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

            // Verificar que el usuario autenticado es el mismo que el que se intenta
            // eliminar
            if (usuario != null && usuario.getIdUsuario().equals(idUsuario)) {
                usuarioService.eliminarUsuarioPorId(idUsuario);
                session.invalidate(); // Cerrar sesión después de eliminar la cuenta
                return new ResponseEntity<>("Cuenta eliminada correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No autorizado para eliminar esta cuenta", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la cuenta: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listaUsuarios")
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerTodosUsuarios();
    }

    @DeleteMapping("/adminEliminaUsuario/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

}
