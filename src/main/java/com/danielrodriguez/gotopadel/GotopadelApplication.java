package com.danielrodriguez.gotopadel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación GoToPádel.
 * Configura y ejecuta la aplicación Spring Boot.
 */
@SpringBootApplication
public class GotopadelApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos pasados al iniciar la aplicación
     */
    public static void main(String[] args) {
        SpringApplication.run(GotopadelApplication.class, args);
    }
}