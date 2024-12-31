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
    private final String parametros = 
        "?user_id=me&playtomic_status=ACTIVE&with_properties=ALLOWS_CASH_PAYMENT" +
        "&sport_id=PADEL&radius=50000&size=40&page=0&coordinate=";

    /**
     * Endpoint para obtener pistas de pádel cercanas a unas coordenadas específicas.
     *
     * @param latitud  Latitud de la ubicación.
     * @param longitud Longitud de la ubicación.
     * @return Respuesta de la API externa en formato JSON.
     */
    @GetMapping
    public ResponseEntity<String> obtenerPistas(@RequestParam String latitud, @RequestParam String longitud) {
        // Construye las coordenadas y la URL completa
        String coordenadas = latitud + "," + longitud;
        String url = urlBase + parametros + coordenadas;

        // Crea un cliente RestTemplate para realizar la solicitud
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;

        try {
            // Realiza la solicitud GET a la API externa
            response = restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            // Lanza una excepción en caso de error al realizar la solicitud
            throw new RuntimeException(e);
        }

        // Devuelve la respuesta obtenida de la API
        return response;
    }
}
