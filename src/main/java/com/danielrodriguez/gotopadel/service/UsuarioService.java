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

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios y sus credenciales.
 */
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

    /**
     * Realiza el inicio de sesión de un usuario.
     *
     * @param email el email del usuario.
     * @param password la contraseña del usuario.
     * @return el objeto Usuario si las credenciales son correctas; de lo contrario, retorna null.
     */
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            return null;
        }

        Optional<Credenciales> credenciales = credencialesRepository.findById(usuario.getIdUsuario());
        return credenciales.filter(cred -> cred.getPassword().equals(password)).map(cred -> usuario).orElse(null);
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario el objeto Usuario a registrar.
     * @param password la contraseña del usuario.
     * @return true si el registro fue exitoso; false si hubo un error o el email ya está registrado.
     */
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

    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return una lista de objetos Usuario.
     */
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Elimina un usuario y sus credenciales asociados, dado su ID.
     *
     * @param idUsuario el ID del usuario a eliminar.
     */
    public void eliminarUsuarioPorId(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
        credencialesRepository.deleteById(idUsuario);
    }

    /**
     * Verifica si un email ya existe en la base de datos.
     *
     * @param email el email a verificar.
     * @return true si el email ya existe; false de lo contrario.
     */
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Valida los campos de un usuario y su contraseña.
     *
     * @param usuario el objeto Usuario con los datos a validar.
     * @param password la contraseña a validar.
     * @return true si todos los campos son válidos; false de lo contrario.
     */
    private boolean validarCampos(Usuario usuario, String password) {
        return patronNombre.matcher(usuario.getNombre()).matches() &&
               patronEmail.matcher(usuario.getEmail()).matches() &&
               patronPassword.matcher(password).matches();
    }
}
