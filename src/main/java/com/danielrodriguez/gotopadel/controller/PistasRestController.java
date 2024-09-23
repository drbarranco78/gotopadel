package com.danielrodriguez.gotopadel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/pistas")
public class PistasRestController {
    //private final String apiKey = "tu_clave_api";
    private final String urlBase = "https://api.playtomic.io/v1/tenants";
    private final String parametros = "?user_id=me&playtomic_status=ACTIVE&with_properties=ALLOWS_CASH_PAYMENT&sport_id=PADEL&radius=50000&size=40&page=0&coordinate=";

    @GetMapping
    public ResponseEntity<String> obtenerPistas(@RequestParam String latitud, @RequestParam String longitud) {
        String coordenadas = latitud + "," + longitud;
        String url = urlBase + parametros + coordenadas;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
