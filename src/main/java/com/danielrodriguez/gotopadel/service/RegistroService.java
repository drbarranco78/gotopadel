package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Credenciales;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.CredencialesRepository;
import com.danielrodriguez.gotopadel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class RegistroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CredencialesRepository credencialesRepository;

    // Expresiones regulares para validar los campos
    private static final Pattern patronNombre = Pattern.compile("^(?=[A-Za-zñÑáéíóúÁÉÍÓÚ])[A-Za-zñÑáéíóúÁÉÍÓÚ\\s]{1,48}[A-Za-zñÑáéíóúÁÉÍÓÚ]$");
    private static final Pattern patronEmail = Pattern.compile("^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\\.[a-zA-Z]{2,}$");
    private static final Pattern patronPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$");

    public boolean registrarUsuario(Usuario usuario, String password) {
        try {
            // Guardar usuario
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            // Verificar si el usuario ha sido guardado correctamente y tiene ID asignado
            if (usuarioGuardado.getIdUsuario() != null) {
                // Crear credenciales y asignar el ID del usuario
                Credenciales credenciales = new Credenciales();
                credenciales.setIdUsuario(usuarioGuardado.getIdUsuario()); // Asignar manualmente el ID del usuario
                credenciales.setPassword(password);

                // Guardar las credenciales
                credencialesRepository.save(credenciales);

                return true;
            } else {
                throw new Exception("Error al guardar el usuario: ID de usuario no asignado.");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Opcional: Log del error para depuración
            return false; // En caso de error, devuelve false
        }
    }


    // Método para validar los campos
    private boolean validarCampos(Usuario usuario, String password) {
        if (!patronNombre.matcher(usuario.getNombre()).matches()) {
            return false; // Nombre inválido
        }
        if (!patronEmail.matcher(usuario.getEmail()).matches()) {
            return false; // Email inválido
        }
        if (!patronPassword.matcher(password).matches()) {
            return false; // Contraseña inválida
        }
        return true;
    }
}
