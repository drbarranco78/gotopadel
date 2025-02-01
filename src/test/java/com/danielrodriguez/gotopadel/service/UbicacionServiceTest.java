package com.danielrodriguez.gotopadel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.repository.UbicacionRepository;

public class UbicacionServiceTest {
    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private UbicacionService ubicacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCrearUbicacionExistente() {
        // Datos de prueba
        String nombreUbicacion = "UbicacionTest";
        String ciudad = "CiudadTest";

        // Simular que la ubicación ya existe en la base de datos
        Ubicacion ubicacionExistente = new Ubicacion();
        ubicacionExistente.setIdUbicacion(1);
        ubicacionExistente.setNombre(nombreUbicacion);
        ubicacionExistente.setCiudad(ciudad);

        when(ubicacionRepository.findUbicacion(nombreUbicacion, ciudad))
                .thenReturn(Optional.of(ubicacionExistente)); // Simula que ya existe la ubicación

        // Intentamos crear la misma ubicación 
        Ubicacion resultado = ubicacionService.createUbicacion(nombreUbicacion, ciudad);

        // Verificación
        assertEquals(null, resultado); // La creación debe devolver null si ya existe

        // Verificar que no se intentó guardar una nueva ubicación
        verify(ubicacionRepository, never()).save(any(Ubicacion.class));
    }
 
}
