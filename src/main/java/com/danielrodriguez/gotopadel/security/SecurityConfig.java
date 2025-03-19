package com.danielrodriguez.gotopadel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para la aplicación Spring Boot.
 * Define las reglas de autorización y configura el filtro de API Key personalizado
 * para proteger las rutas de la aplicación.
 */
@Configuration
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    /**
     * Constructor que inyecta el filtro de API Key personalizado.
     *
     * @param apiKeyFilter el filtro de API Key que se aplicará a las solicitudes
     */
    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * Deshabilita CSRF, define las reglas de autorización para las rutas y
     * añade el filtro de API Key antes del filtro de autenticación por usuario/contraseña.
     *
     * @param http el objeto HttpSecurity usado para configurar las reglas de seguridad
     * @return el objeto SecurityFilterChain configurado
     * @throws Exception si ocurre un error durante la configuración de la seguridad
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para APIs
                .authorizeHttpRequests(auth -> auth                  
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/admin/**", "/private/**").permitAll()
                        .anyRequest().permitAll())
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class) 
                .build();
    }
}