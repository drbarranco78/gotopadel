package com.danielrodriguez.gotopadel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Controlador REST para gestionar las noticias relacionadas con el pádel.
 * Proporciona un endpoint para obtener noticias desde una API externa.
 */
@RestController
@RequestMapping("/api/noticias")
public class NoticiasRestController {

    // Clave de API para autenticar las solicitudes a la API de noticias
    private final String API_KEY = "0f69ce3efa49459ea3b53fb57d7ec630";
    // URL de la API de noticias, configurada para buscar noticias relacionadas con el pádel en español
    private final String URL = "https://newsapi.org/v2/everything?qInTitle=padel&language=es&apiKey=" + API_KEY;

    /**
     * Endpoint para obtener noticias sobre pádel.
     *
     * @return Una respuesta HTTP que contiene las noticias en formato JSON.
     */
    @GetMapping
    public ResponseEntity<String> obtenerNoticias() {
        // Objeto RestTemplate para realizar solicitudes HTTP a la API externa
        RestTemplate restTemplate = new RestTemplate();
        String response = null;

        try {
            // Realiza una solicitud GET a la URL configurada y almacena la respuesta como una cadena JSON
            response = restTemplate.getForObject(URL, String.class);
        } catch (RestClientException e) {
            // Manejo de excepciones en caso de errores al realizar la solicitud HTTP
            throw new RuntimeException(e);
        }

        // Devuelve la respuesta de la API externa como una respuesta HTTP 200 (OK)
        return ResponseEntity.ok(response);
    }
}
