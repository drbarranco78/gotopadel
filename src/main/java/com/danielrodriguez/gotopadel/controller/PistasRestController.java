package com.danielrodriguez.gotopadel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador REST para obtener información sobre pistas de pádel.
 * Este controlador interactúa con la API externa de Playtomic.
 */
@RestController
@RequestMapping("/api/pistas")
public class PistasRestController {

    // URL base para la API de Playtomic
    private final String urlBase = "https://api.playtomic.io/v1/tenants";

    // Parámetros adicionales para las solicitudes a la API
    private final String parametros = "?user_id=me&playtomic_status=ACTIVE&with_properties=ALLOWS_CASH_PAYMENT" +
            "&sport_id=PADEL&radius=50000&size=40&page=0&coordinate=";

    /**
     * Endpoint para obtener pistas de pádel cercanas a unas coordenadas
     * específicas.
     *
     * @param latitud  Latitud de la ubicación.
     * @param longitud Longitud de la ubicación.
     * @return Respuesta de la API externa en formato JSON.
     */
    @GetMapping
    public ResponseEntity<String> obtenerPistas(
            @RequestParam String latitud,
            @RequestParam String longitud,
            @RequestParam(defaultValue = "10") int size, // Tamaño de la página (por defecto 10)
            @RequestParam(defaultValue = "0") int page // Página (por defecto 0)
    ) {
        String coordenadas = latitud + "," + longitud;
        String url = urlBase + "?user_id=me&playtomic_status=ACTIVE&with_properties=ALLOWS_CASH_PAYMENT" +
                "&sport_id=PADEL&radius=50000&size=" + size + "&page=" + page + "&coordinate=" + coordenadas;

        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

}
