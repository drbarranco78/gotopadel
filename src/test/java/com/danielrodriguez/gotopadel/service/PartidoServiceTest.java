package com.danielrodriguez.gotopadel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.repository.PartidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PartidoServiceTest {
    @Mock
    private PartidoRepository partidoRepository;

    private PartidoService partidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        partidoService = new PartidoService(partidoRepository);
    }   

    @Test
    void testContarPartidosPublicadosPorUsuario() {
        // Datos de prueba
        int idUsuarioAnaSanchez = 5;

        // Simulamos el comportamiento del repositorio: Ana tiene 3 partidos publicados
        when(partidoRepository.countPartidosPublicadosPorUsuario(idUsuarioAnaSanchez)).thenReturn(3);

        // Llamada al método del servicio
        int cantidadPartidosPublicados = partidoService.contarPartidosPublicadosPorUsuario(idUsuarioAnaSanchez);

        // Verificación
        assertEquals(3, cantidadPartidosPublicados, "El número de partidos publicados no coincide con el esperado.");
    }

    @Test
    void testDescontarVacante() {
        // Datos de prueba
        int idPartido = 1;
        byte vacantesIniciales = 3;
        Partido partido = new Partido();
        partido.setIdPartido(idPartido);
        partido.setVacantes(vacantesIniciales);

        // Simulación del repositorio
        when(partidoRepository.findById(idPartido)).thenReturn(Optional.of(partido));

        // Llamada al método
        partidoService.descontarVacante(idPartido);

        // Verificación
        assertEquals((int) (vacantesIniciales - 1), (int) partido.getVacantes(),
                "Las vacantes no se decrementaron correctamente");

        System.out.println(partido.getVacantes());

        // Verificar que se guardó el cambio
        verify(partidoRepository, times(1)).save(partido);
    }

    @Test
    void testObtenerPartidoPorId_noExiste() {
        // Asignamos un ID que no existe en la base de datos
        int idInexistente = 9999;

        // Llamar al método
        Optional<Partido> resultado = partidoService.obtenerPartidoPorId(idInexistente);

        // Verificar que el resultado sea vacío 
        assertTrue(resultado.isEmpty(), "El partido no debería existir en la base de datos.");
    }
    @Test
    void testEliminarPartido_noExiste() {
        // Asignamos un ID que no existe en la base de datos
        int idInexistente = 99999;
        
        // Llamar al método
        boolean resultado = partidoService.eliminarPartido(idInexistente);
        
        // Verificar que devuelve false
        assertFalse(resultado, "No se debería poder eliminar un partido que no existe.");
    }  
}
