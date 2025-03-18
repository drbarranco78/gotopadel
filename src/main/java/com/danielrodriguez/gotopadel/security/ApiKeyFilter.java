package com.danielrodriguez.gotopadel.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class ApiKeyFilter extends GenericFilterBean {

    @Value("${api.key}") // Recupera la clave desde application.properties
    private String validApiKey;

    private static final String API_KEY_HEADER = "X-API-KEY";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Obtén la ruta de la solicitud
        String path = httpRequest.getServletPath();

       
        // Solo aplica el filtro a rutas que empiecen por /api/
        if (!path.startsWith("/api/") || path.startsWith("/api/pistas")) {
            chain.doFilter(request, response);
            return;
        }

        // Recupera la API Key del header
        String apiKey = httpRequest.getHeader(API_KEY_HEADER);

        // Si la API Key es nula o incorrecta, rechaza la solicitud
        if (apiKey == null || !apiKey.equals(validApiKey)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("API Key inválida");
            return;
        }

        // Si la clave es válida, continúa con la solicitud
        chain.doFilter(request, response);
    }

}
