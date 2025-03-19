package com.danielrodriguez.gotopadel.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para obtener información sobre pistas de pádel.
 * Este controlador interactúa con la API externa de Playtomic.
 */
@RestController
@RequestMapping("/api/pistas")
public class PistasRestController {

    private static final Logger logger = LoggerFactory.getLogger(PistasRestController.class);

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
     * @return Respuesta de la API externa en formato JSON o un mensaje de error si falla.
     */
    @GetMapping
    public ResponseEntity<?> obtenerPistas(@RequestParam String latitud, @RequestParam String longitud) {
        String coordenadas = latitud + "," + longitud;
        String url = urlBase + parametros + coordenadas;

        RestTemplate restTemplate = new RestTemplate();
        logger.info("Iniciando solicitud a Playtomic: {}", url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            logger.info("Respuesta recibida de Playtomic: {}", response.getStatusCode());
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            logger.error("Error al conectar con Playtomic: {}", e.getMessage(), e);

            // Crea un objeto JSON con el mensaje de error
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo conectar con Playtomic");
            errorResponse.put("cause", e.getMessage());

            
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(errorResponse);
        } catch (Exception e) {
            logger.error("Error inesperado en el endpoint: {}", e.getMessage(), e);

            // Error genérico para otros casos
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("cause", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                    .body(errorResponse);
        }
    }
}