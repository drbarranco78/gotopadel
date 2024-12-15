package com.danielrodriguez.gotopadel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.danielrodriguez.gotopadel.model.Usuario;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/")
public class VistasController {

    @GetMapping("/")
    public String home(HttpSession session) {
        session.invalidate();  // Invalida la sesión al acceder a la página de inicio
        return "index";
    }

    @GetMapping("/zonaPrivada")
    public String mostrarZonaPrivada(HttpSession session) {
        Object usuario = session.getAttribute("usuarioAutenticado");
        //if (usuario != null) {
            return "zonaPrivada";  // Usuario autenticado, mostrar zona privada
        //}
        //return "redirect:/";  // Redirigir a la página de inicio si no hay sesión
    }

    @GetMapping("/administrador")
    public String mostrarZonaAdministrador(HttpSession session) {
        Object usuario = session.getAttribute("usuarioAutenticado");
        if (usuario != null && esAdmin(usuario)) {
            return "administrador";  // Usuario autenticado y es admin
        }
        return "redirect:/";  // Redirigir a la página de inicio si no es admin o no está autenticado
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    private boolean esAdmin(Object usuario) {
        // Verifica si el usuario tiene rol de administrador
        // Supongamos que tu objeto Usuario tiene un método getRole()
        if (usuario instanceof Usuario) {
            Usuario u = (Usuario) usuario;
            return "ADMIN".equals(u.getRol());
        }
        return false;
    }
}