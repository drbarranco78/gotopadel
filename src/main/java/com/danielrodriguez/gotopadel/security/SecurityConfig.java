package com.danielrodriguez.gotopadel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para APIs
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers("/api/usuario/listaUsuarios").permitAll()
                        //.requestMatchers("/api/**").authenticated() // Protege solo rutas de API
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/admin/**", "/private/**").permitAll()

                        .anyRequest().permitAll())
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class) 
                .build();
    }
}
