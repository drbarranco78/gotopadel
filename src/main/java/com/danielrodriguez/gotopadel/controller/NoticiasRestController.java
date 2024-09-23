package com.danielrodriguez.gotopadel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/noticias")
public class NoticiasRestController {
    private final String API_KEY = "0f69ce3efa49459ea3b53fb57d7ec630";
    private final String URL = "https://newsapi.org/v2/everything?qInTitle=padel&language=es&apiKey=" + API_KEY;

    @GetMapping
    public ResponseEntity<String> obtenerNoticias() {
        RestTemplate restTemplate = new RestTemplate();
        String response = null;
        try {
            response = restTemplate.getForObject(URL, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }

}
