package com.danielrodriguez.gotopadel.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.CredencialesRepository;
import com.danielrodriguez.gotopadel.repository.UsuarioRepository;

public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CredencialesRepository credencialesRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    public UsuarioServiceTest() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    void testRegistrarUsuario() {
        // Configuración del usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Daniel");
        usuario.setEmail("daniel@test.com");
        String password = "Password123";

        // Validación        
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario savedUsuario = invocation.getArgument(0);
            // Simula que la base de datos asigna el ID 1
            savedUsuario.setIdUsuario(1); 
            return savedUsuario;
        });      

        // Ejecución del método
        boolean resultado = usuarioService.registrarUsuario(usuario, password);

        // Verificación
        assertTrue(resultado, "El registro del usuario debe devolver true");        
    }

}
