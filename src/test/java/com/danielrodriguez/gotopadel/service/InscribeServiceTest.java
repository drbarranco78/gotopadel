package com.danielrodriguez.gotopadel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.danielrodriguez.gotopadel.model.Inscribe;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Usuario;
import com.danielrodriguez.gotopadel.repository.InscribeRepository;

public class InscribeServiceTest {
    @Mock
    private InscribeRepository inscribeRepository;

    @InjectMocks
    private InscribeService inscribeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }  


    @Test
    void testInscribirUsuarioDosVeces() {
        // Datos de prueba
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        Partido partido = new Partido();
        partido.setIdPartido(10);

        Inscribe inscripcion = new Inscribe();
        inscripcion.setUsuario(usuario);
        inscripcion.setPartido(partido);

        // Simular que la primera inscripción es exitosa
        when(inscribeRepository.existsByUsuario_idAndPartido_idPartido(1, 10)).thenReturn(false);
        when(inscribeRepository.save(inscripcion)).thenReturn(inscripcion);

        // Primera inscripción
        Inscribe primeraInscripcion = inscribeService.inscribir(inscripcion);
        assertNotNull(primeraInscripcion, "La primera inscripción debería ser exitosa.");

        // Simular que el usuario ya está inscrito en el partido
        when(inscribeRepository.existsByUsuario_idAndPartido_idPartido(1, 10)).thenReturn(true);

        // Segunda inscripción
        Inscribe segundaInscripcion = inscribeService.inscribir(inscripcion);
        assertNull(segundaInscripcion, "La segunda inscripción debería devolver null.");
    }

    @Test
    void testInscribirJugadorYVerificarInscripcion() {
        // Datos de prueba
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);

        Partido partido = new Partido();
        partido.setIdPartido(10);

        Inscribe inscripcion = new Inscribe();
        inscripcion.setUsuario(usuario);
        inscripcion.setPartido(partido);

        // Simular que el usuario no está inscrito antes
        when(inscribeRepository.existsByUsuario_idAndPartido_idPartido(1, 10)).thenReturn(false);
        when(inscribeRepository.save(inscripcion)).thenReturn(inscripcion);

        // Intentar inscribirlo
        Inscribe resultado = inscribeService.inscribir(inscripcion);
        assertNotNull(resultado, "La inscripción debería haberse realizado con éxito.");

        // Simular que el usuario ahora está inscrito
        when(inscribeRepository.existsByUsuario_idAndPartido_idPartido(1, 10)).thenReturn(true);

        // Verificar si está inscrito
        boolean estaInscrito = inscribeService.verificarInscripcion(1, 10);
        assertTrue(estaInscrito, "El usuario debería estar inscrito en el partido.");
    }

    @Test
    void testObtenerCantidadInscripciones() {
        int idUsuario = 1;

        // Simular que el usuario está inscrito en 3 partidos
        when(inscribeRepository.countByUsuario_id(idUsuario)).thenReturn(3);

        // Verificar la cantidad de inscripciones
        int cantidadInscripciones = inscribeService.obtenerCantidadInscripciones(idUsuario);
        assertEquals(3, cantidadInscripciones, "El usuario debería estar inscrito en 3 partidos.");
    }
}
