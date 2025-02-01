package com.danielrodriguez.gotopadel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.danielrodriguez.gotopadel.repository.PartidoRepository;

public class ArchivoServiceTest {

    @Mock
    private PartidoRepository partidoRepository;

    @InjectMocks
    private ArchivoService archivoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }    

    @Test
    void testArchivarPartido_noExiste() {
        // ID de un partido que no existe
        int idInexistente = 9999;
        int motivo = 1;

        // Simulamos que el repositorio no encuentra el partido
        when(partidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Verificamos que al llamar a archivarPartido se lanza la excepción esperada
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            archivoService.archivarPartido(idInexistente, motivo);
        });

        // Comprobamos que el mensaje de la excepción es el correcto
        assertEquals("Partido no encontrado", exception.getMessage());
    }
}
