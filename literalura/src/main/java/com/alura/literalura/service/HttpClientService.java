package com.alura.literalura.service;

import com.alura.literalura.config.ApiConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class HttpClientService {

    private final HttpClient httpClient;

    public HttpClientService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Realizar solicitud HTTP síncrona
     */
    public String realizarSolicitud(String url) throws IOException, InterruptedException {
        HttpRequest request = construirRequest(url);
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        validarRespuesta(response);
        return response.body();
    }

    /**
     * Realizar solicitud HTTP asíncrona
     */
    public CompletableFuture<String> realizarSolicitudAsincrona(String url) {
        HttpRequest request = construirRequest(url);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        validarRespuesta(response);
                        return response.body();
                    } catch (Exception e) {
                        throw new RuntimeException("Error en solicitud asíncrona: " + e.getMessage(), e);
                    }
                });
    }

    /**
     * Construir HttpRequest con configuraciones estándar
     */
    private HttpRequest construirRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(ApiConfig.TIMEOUT_SECONDS))
                .header("Accept", "application/json")
                .header("User-Agent", ApiConfig.USER_AGENT)
                .header("Accept-Encoding", "gzip, deflate")
                .GET()
                .build();
    }

    /**
     * Validar respuesta HTTP
     */
    private void validarRespuesta(HttpResponse<String> response) {
        int statusCode = response.statusCode();

        if (statusCode >= 200 && statusCode < 300) {
            // Respuesta exitosa
            System.out.println("✅ Solicitud exitosa - Código: " + statusCode);
            return;
        }

        // Manejar diferentes tipos de errores
        String mensajeError = switch (statusCode) {
            case 400 -> "Solicitud incorrecta - Parámetros inválidos";
            case 404 -> "Recurso no encontrado";
            case 429 -> "Demasiadas solicitudes - Rate limit excedido";
            case 500 -> "Error interno del servidor";
            default -> "Error HTTP: " + statusCode;
        };

        throw new RuntimeException(mensajeError + " - Respuesta: " + response.body());
    }

    /**
     * Mostrar información detallada de la respuesta (para debugging)
     */
    public void mostrarInformacionRespuesta(HttpResponse<String> response) {
        System.out.println("=== INFORMACIÓN DE RESPUESTA ===");
        System.out.println("Código de estado: " + response.statusCode());
        System.out.println("URI: " + response.uri());

        System.out.println("Headers:");
        response.headers().map().forEach((key, value) -> System.out.println("  " + key + ": " + value));

        System.out.println("Tamaño del cuerpo: " + response.body().length() + " caracteres");
        System.out.println("================================");
    }
}
