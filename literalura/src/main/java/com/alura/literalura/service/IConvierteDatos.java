package com.alura.literalura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

    String convertirAJson(Object objeto);

    <T> T obtenerDatosConLog(String json, Class<T> clase);
}
