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

/**
 * Filtro de seguridad que valida la presencia y corrección de una API Key en las solicitudes entrantes.
 * Este filtro se aplica a las rutas que comienzan por "/api/", excluyendo las rutas de "/api/pistas".
 * Si la API Key no es válida o no está presente, se rechaza la solicitud con un estado HTTP 401 (Unauthorized).
 */
@Component
public class ApiKeyFilter extends GenericFilterBean {

    @Value("${api.key}") // Recupera la clave desde application.properties
    private String validApiKey;

    private static final String API_KEY_HEADER = "X-API-KEY";
    /**
     * Constructor por defecto para la inicialización del filtro.
     */
    public ApiKeyFilter() {
        
    }

    /**
     * Procesa las solicitudes entrantes para validar la API Key en el encabezado de la solicitud.
     * Si la solicitud no pertenece a una ruta protegida o la API Key es válida, permite que la solicitud continúe.
     * En caso contrario, retorna una respuesta de error con estado 401.
     *
     * @param request la solicitud entrante recibida por el filtro
     * @param response la respuesta que se enviará al cliente
     * @param chain el objeto FilterChain que permite pasar la solicitud al siguiente filtro o controlador
     * @throws IOException si ocurre un error de entrada/salida durante el procesamiento
     * @throws ServletException si ocurre un error relacionado con el servlet durante el procesamiento
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Obtén la ruta de la solicitud
        String path = httpRequest.getServletPath();

        // Solo aplica el filtro a rutas que empiecen por /api/
        if (!path.startsWith("/api/") || path.startsWith("/api/pistas") || 
        path.startsWith("/api/usuario/config") || path.startsWith("/api/noticias")) {
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