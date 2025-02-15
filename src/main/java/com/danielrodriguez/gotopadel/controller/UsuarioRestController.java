package com.danielrodriguez.gotopadel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.PartidoService;
import com.danielrodriguez.gotopadel.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador REST para gestionar operaciones relacionadas con usuarios.
 * Incluye funcionalidad para login, registro, gestión de cuentas y estadísticas.
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioRestController {

    @Autowired
    // Servicio para operaciones de usuarios
    private UsuarioService usuarioService; 
    
    // Servicio para operaciones relacionadas con partidos
    private PartidoService partidoService; 

    /**
     * Constructor para inyección de dependencias.
     *
     * @param usuarioService Servicio de usuarios.
     * @param partidoService Servicio de partidos.
     */
    public UsuarioRestController(UsuarioService usuarioService, PartidoService partidoService) {
        this.usuarioService = usuarioService;
        this.partidoService = partidoService;
    }

    /**
     * Endpoint para el inicio de sesión de un usuario.
     *
     * @param loginDatos Datos del login (email y contraseña).
     * @param session    Sesión HTTP para guardar el usuario autenticado.
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginDatos, HttpSession session) {
        String email = loginDatos.get("email");
        String password = loginDatos.get("password");

        Usuario usuario = usuarioService.login(email, password);

        if (usuario != null) {
            session.setAttribute("usuarioAutenticado", usuario); // Guarda el usuario en la sesión
            if (email.equals("admin@email.com")) {
                usuario.setRol("ADMIN"); // Asigna rol de administrador
                return new ResponseEntity<>("Login exitoso, redirigiendo a admin.html", HttpStatus.OK);
            }
            return new ResponseEntity<>("Login exitoso, redirigiendo a private.html", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint para cerrar sesión.
     *
     * @param session Sesión HTTP que se debe invalidar.
     * @return Mensaje de cierre de sesión exitoso.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión actual
        return new ResponseEntity<>("Sesión cerrada correctamente", HttpStatus.OK);
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param registroDatos Datos del registro del usuario.
     * @param sesion        Sesión HTTP para guardar el nuevo usuario autenticado.
     * @return Respuesta con información del usuario registrado o error.
     */
    @PostMapping("/registrarUsuario")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody Map<String, String> registroDatos, HttpSession sesion) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDatos.get("nombre"));
        nuevoUsuario.setEmail(registroDatos.get("email"));
        nuevoUsuario.setGenero(registroDatos.get("genero"));
        nuevoUsuario.setNivel(registroDatos.get("nivel"));
        nuevoUsuario.setFechaNac(registroDatos.get("fechaNac"));
        nuevoUsuario.setFechaInscripcion(registroDatos.get("fechaInscripcion"));

        if (usuarioService.emailExiste(registroDatos.get("email"))) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "El email ya está registrado");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        boolean registroExitoso = usuarioService.registrarUsuario(nuevoUsuario, registroDatos.get("password"));

        if (registroExitoso) {
            sesion.setAttribute("usuarioAutenticado", nuevoUsuario);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("nombreUsuario", nuevoUsuario.getNombre());
            respuesta.put("idUsuario", nuevoUsuario.getIdUsuario().toString());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint para obtener los datos del usuario autenticado.
     *
     * @param session Sesión HTTP del usuario autenticado.
     * @return Datos del usuario o error si no está autenticado.
     */
    @GetMapping("/datosUsuario")
    public ResponseEntity<Usuario> obtenerDatosUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint para eliminar la cuenta del usuario autenticado.
     *
     * @param idUsuario ID del usuario que se desea eliminar.
     * @param session   Sesión HTTP del usuario.
     * @return Mensaje de éxito o error.
     */
    @DeleteMapping("/eliminarCuenta/{idUsuario}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable Integer idUsuario, HttpSession session) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
            if (usuario != null && usuario.getIdUsuario().equals(idUsuario)) {
                usuarioService.eliminarUsuarioPorId(idUsuario);
                session.invalidate();
                return new ResponseEntity<>("Cuenta eliminada correctamente", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No autorizado para eliminar esta cuenta", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar la cuenta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener una lista de todos los usuarios.
     *
     * @return Lista de usuarios.
     */
    @GetMapping("/listaUsuarios")
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerTodosUsuarios();
    }

    /**
     * Endpoint para que un administrador elimine un usuario específico.
     *
     * @param id ID del usuario a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/adminEliminaUsuario/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para contar los partidos publicados por un usuario.
     *
     * @param idUsuario ID del usuario.
     * @return Número de partidos publicados por el usuario.
     */
    @GetMapping("/{idUsuario}/publicados/count")
    public int contarPartidosPublicados(@PathVariable Integer idUsuario) {
        return partidoService.contarPartidosPublicadosPorUsuario(idUsuario);
    }
    
}
