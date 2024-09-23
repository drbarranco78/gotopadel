package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Credenciales;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.CredencialesRepository;
import com.danielrodriguez.gotopadel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CredencialesRepository credencialesRepository;

    public Usuario login(String email, String password) {
        // Buscar al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return null; // El usuario no existe
        }
        // Buscar las credenciales de ese usuario
        Optional<Credenciales> credenciales = credencialesRepository.findById(usuario.getIdUsuario());

        // Validar la contraseña
        try {
            if(credenciales.isPresent() && credenciales.get().getPassword().equals(password)){
                return usuario;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException(e);
        }
    }

}
