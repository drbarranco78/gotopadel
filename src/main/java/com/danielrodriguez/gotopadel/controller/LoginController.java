package com.danielrodriguez.gotopadel.controller;

import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody Map<String, String> loginDatos, HttpSession session) {
        String email = loginDatos.get("email");
        String password = loginDatos.get("password");

        Usuario usuario = loginService.login(email, password);

        if (usuario != null) {
            // Si el login es exitoso, guardar datos en la sesión
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("nombreUsuario", usuario.getNombre());

            return new ResponseEntity<>("Login exitoso, redirigiendo a zonaPersonal.html", HttpStatus.OK);
        } else {
            // Si las credenciales son incorrectas
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }
    }

}
