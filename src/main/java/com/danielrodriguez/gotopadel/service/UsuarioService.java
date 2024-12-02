package com.danielrodriguez.gotopadel.service;

import com.danielrodriguez.gotopadel.model.Credenciales;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.CredencialesRepository;
import com.danielrodriguez.gotopadel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CredencialesRepository credencialesRepository;

    // Expresiones regulares para validar los campos
    private static final Pattern patronNombre = Pattern.compile("^(?=[A-Za-zñÑáéíóúÁÉÍÓÚ])[A-Za-zñÑáéíóúÁÉÍÓÚ\\s]{1,48}[A-Za-zñÑáéíóúÁÉÍÓÚ]$");
    private static final Pattern patronEmail = Pattern.compile("^[a-zA-Z0-9._%+-]{1,40}@[a-zA-Z0-9.-]{2,20}\\.[a-zA-Z]{2,}$");
    private static final Pattern patronPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$");

    // Método para iniciar sesión
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return null;
        }

        Optional<Credenciales> credenciales = credencialesRepository.findById(usuario.getIdUsuario());
        return credenciales.filter(cred -> cred.getPassword().equals(password)).map(cred -> usuario).orElse(null);
    }

    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(Usuario usuario, String password) {
        try {
            if (!validarCampos(usuario, password) || usuarioRepository.existsByEmail(usuario.getEmail())) {
                return false; // Datos inválidos o email ya registrado
            }

            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            Credenciales credenciales = new Credenciales();
            credenciales.setIdUsuario(usuarioGuardado.getIdUsuario());
            credenciales.setPassword(password);
            credencialesRepository.save(credenciales);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Método para eliminar un usuario por ID
    public void eliminarUsuarioPorId(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
        credencialesRepository.deleteById(idUsuario);
    }
    

    // Validación de campos
    private boolean validarCampos(Usuario usuario, String password) {
        return patronNombre.matcher(usuario.getNombre()).matches() &&
               patronEmail.matcher(usuario.getEmail()).matches() &&
               patronPassword.matcher(password).matches();
    }

    // Método para verificar si un email ya existe
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
