package com.alura.literalura.service;

import com.alura.literalura.config.ApiConfig;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.RespuestaLibros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GutendxServiceMejorado {

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private ConvierteDatos conversor;

    /**
     * Buscar libros y convertir a objetos Java
     */
    public RespuestaLibros buscarLibros(String titulo) throws IOException, InterruptedException {
        String tituloEncoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = ApiConfig.BASE_URL + "?search=" + tituloEncoded;

        System.out.println("🔍 Buscando libros: " + titulo);

        String jsonRespuesta = httpClientService.realizarSolicitud(url);
        return conversor.obtenerDatosConLog(jsonRespuesta, RespuestaLibros.class);
    }

    /**
     * Obtener libro específico por ID
     */
    public Libro obtenerLibroPorId(Long id) throws IOException, InterruptedException {
        String url = ApiConfig.BASE_URL + id + "/";

        System.out.println("📖 Obteniendo libro ID: " + id);

        String jsonRespuesta = httpClientService.realizarSolicitud(url);
        return conversor.obtenerDatosConLog(jsonRespuesta, Libro.class);
    }

    /**
     * Buscar libros por autor
     */
    public List<Libro> buscarPorAutor(String autor) throws IOException, InterruptedException {
        RespuestaLibros respuesta = buscarLibros(autor);
        return respuesta.getLibros();
    }

    /**
     * Buscar libros por idioma
     */
    public RespuestaLibros buscarPorIdioma(String idioma) throws IOException, InterruptedException {
        String url = ApiConfig.BASE_URL + "?languages=" + idioma;

        System.out.println("🌍 Buscando libros en idioma: " + idioma);

        String jsonRespuesta = httpClientService.realizarSolicitud(url);
        return conversor.obtenerDatosConLog(jsonRespuesta, RespuestaLibros.class);
    }
}
