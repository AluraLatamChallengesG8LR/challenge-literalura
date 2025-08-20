package com.alura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConvierteDatos implements IConvierteDatos {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a " + clase.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String convertirAJson(Object objeto) {
        try {
            return objectMapper.writeValueAsString(objeto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir objeto a JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T obtenerDatosConLog(String json, Class<T> clase) {
        System.out.println("🔄 Convirtiendo JSON a " + clase.getSimpleName() + "...");
        System.out.println("📄 JSON recibido (primeros 200 caracteres): " +
                json.substring(0, Math.min(200, json.length())) + "...");

        T resultado = obtenerDatos(json, clase);

        System.out.println("✅ Conversión exitosa a " + clase.getSimpleName());
        return resultado;
    }
}
