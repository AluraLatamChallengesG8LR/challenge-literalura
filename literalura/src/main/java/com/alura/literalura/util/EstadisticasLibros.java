package com.alura.literalura.util;

import com.alura.literalura.model.Libro;

import java.util.Map;

public class EstadisticasLibros {
    private int totalLibros;
    private int totalDescargas;
    private double promedioDescargas;
    private Map<String, Long> idiomasFrecuencia;
    private Map<String, Long> autoresFrecuencia;
    private Libro libroMasDescargado;

    // Constructor vacío
    public EstadisticasLibros() {
    }

    // Constructor completo
    public EstadisticasLibros(int totalLibros, int totalDescargas, double promedioDescargas,
            Map<String, Long> idiomasFrecuencia, Map<String, Long> autoresFrecuencia,
            Libro libroMasDescargado) {
        this.totalLibros = totalLibros;
        this.totalDescargas = totalDescargas;
        this.promedioDescargas = promedioDescargas;
        this.idiomasFrecuencia = idiomasFrecuencia;
        this.autoresFrecuencia = autoresFrecuencia;
        this.libroMasDescargado = libroMasDescargado;
    }

    // Getters y Setters
    public int getTotalLibros() {
        return totalLibros;
    }

    public void setTotalLibros(int totalLibros) {
        this.totalLibros = totalLibros;
    }

    public int getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(int totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    public double getPromedioDescargas() {
        return promedioDescargas;
    }

    public void setPromedioDescargas(double promedioDescargas) {
        this.promedioDescargas = promedioDescargas;
    }

    public Map<String, Long> getIdiomasFrecuencia() {
        return idiomasFrecuencia;
    }

    public void setIdiomasFrecuencia(Map<String, Long> idiomasFrecuencia) {
        this.idiomasFrecuencia = idiomasFrecuencia;
    }

    public Map<String, Long> getAutoresFrecuencia() {
        return autoresFrecuencia;
    }

    public void setAutoresFrecuencia(Map<String, Long> autoresFrecuencia) {
        this.autoresFrecuencia = autoresFrecuencia;
    }

    public Libro getLibroMasDescargado() {
        return libroMasDescargado;
    }

    public void setLibroMasDescargado(Libro libroMasDescargado) {
        this.libroMasDescargado = libroMasDescargado;
    }
}
