package com.danielrodriguez.gotopadel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danielrodriguez.gotopadel.model.Usuario;

import jakarta.servlet.http.HttpSession;

/**
 * Controlador para gestionar las vistas de la aplicación GoToPádel.
 */
@Controller
@RequestMapping("/")
public class VistasController {

    /**
     * Maneja la ruta principal del sitio.
     * Invalida la sesión actual para garantizar un nuevo inicio.
     *
     * @param session La sesión HTTP actual.
     * @return La vista de la página de inicio (index).
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        session.invalidate();  // Invalida la sesión al acceder a la página de inicio.
        return "index";
    }

    /**
     * Muestra la zona privada si el usuario está autenticado.
     *
     * @param session La sesión HTTP actual.
     * @return La vista de la zona privada o redirección a la página de inicio.
     */
    @GetMapping("/private")
    public String mostrarZonaPrivada(HttpSession session) {
        Object usuario = session.getAttribute("usuarioAutenticado");
        if (usuario != null) {
            return "private";  // Usuario autenticado, mostrar zona privada.
        }
        return "redirect:/";  // Redirigir a la página de inicio si no hay sesión.
    }

    /**
     * Muestra la zona de administración si el usuario es administrador.
     *
     * @param session La sesión HTTP actual.
     * @return La vista de administración o redirección a la página de inicio.
     */
    @GetMapping("/admin")
    public String mostrarZonaAdministrador(HttpSession session) {
        Object usuario = session.getAttribute("usuarioAutenticado");
        if (usuario != null && esAdmin(usuario)) {
            return "admin";  // Usuario autenticado y es administrador.
        }
        return "redirect:/";  // Redirigir a la página de inicio si no es admin o no está autenticado.
    }

    /**
     * Muestra la página de error.
     *
     * @return La vista de error.
     */
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    /**
     * Verifica si el usuario autenticado tiene rol de administrador.
     *
     * @param usuario El objeto usuario de la sesión.
     * @return {@code true} si el usuario tiene rol de administrador, {@code false} en caso contrario.
     */
    private boolean esAdmin(Object usuario) {
        if (usuario instanceof Usuario) {
            Usuario u = (Usuario) usuario;
            return "ADMIN".equals(u.getRol());
        }
        return false;
    }
}
