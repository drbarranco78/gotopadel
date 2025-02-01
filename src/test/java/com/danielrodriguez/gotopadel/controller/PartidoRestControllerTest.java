package com.danielrodriguez.gotopadel.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.danielrodriguez.gotopadel.model.Partido;
import com.danielrodriguez.gotopadel.model.Ubicacion;
import com.danielrodriguez.gotopadel.service.PartidoService;

@WebMvcTest(PartidoRestController.class)
public class PartidoRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidoService partidoService; // Se mockea la dependencia del servicio

    @Test
    void testGuardarPartido() throws Exception {
        // Configuración del partido
        Partido partido = new Partido();
        partido.setIdPartido(1);
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombre("Central Pádel Granada");
        ubicacion.setCiudad("Granada");
        partido.setUbicacion(ubicacion);

        // Simular comportamiento del servicio
        when(partidoService.guardarPartido(any(Partido.class))).thenReturn(partido);

        // Ejecutar la solicitud POST
        mockMvc.perform(post("/api/partido")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "ubicacion": {
                                "nombre": "Central Pádel Granada",
                                "ciudad": "Granada"
                            }
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPartido").value(1))
                .andExpect(jsonPath("$.ubicacion.nombre").value("Central Pádel Granada"))
                .andExpect(jsonPath("$.ubicacion.ciudad").value("Granada"));
                
    }

}
