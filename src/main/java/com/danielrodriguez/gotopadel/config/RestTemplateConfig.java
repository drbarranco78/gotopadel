// package com.danielrodriguez.gotopadel.config;

// import org.apache.http.client.HttpClient;
// import org.apache.http.client.config.RequestConfig;
// import org.apache.http.impl.client.HttpClients;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
// import org.springframework.web.client.RestTemplate;

// @Configuration
// public class RestTemplateConfig {

//     @Bean
//     public RestTemplate restTemplate() {
//         // Configura los timeouts usando RequestConfig
//         RequestConfig requestConfig = RequestConfig.custom()
//             .setConnectTimeout(5000) // 5 segundos para conectar
//             .setConnectionRequestTimeout(5000) // 5 segundos para obtener una conexión del pool
//             .setSocketTimeout(10000) // 10 segundos para leer la respuesta
//             .build();

//         // Crea un HttpClient con la configuración de timeouts
//         HttpClient httpClient = HttpClients.custom()
//             .setDefaultRequestConfig(requestConfig)
//             .build();

//         // Configura la fábrica con el HttpClient
//         HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//         factory.setHttpClient(httpClient);

//         // Retorna el RestTemplate con la fábrica configurada
//         return new RestTemplate(factory);
//     }
// }