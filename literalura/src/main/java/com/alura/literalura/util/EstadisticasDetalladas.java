package com.alura.literalura.util;

import com.alura.literalura.entity.LibroEntity;

import java.util.List;

public class EstadisticasDetalladas {
    private int totalLibros;
    private int librosConAutor;
    private int librosConDescargas;
    private int totalDescargas;
    private double promedioDescargas;
    private LibroEntity libroMasDescargado;
    private List<String> idiomasUnicos;
    private List<String> autoresUnicos;

    // Constructores
    public EstadisticasDetalladas() {
    }

    // Getters y Setters
    public int getTotalLibros() {
        return totalLibros;
    }

    public void setTotalLibros(int totalLibros) {
        this.totalLibros = totalLibros;
    }

    public int getLibrosConAutor() {
        return librosConAutor;
    }

    public void setLibrosConAutor(int librosConAutor) {
        this.librosConAutor = librosConAutor;
    }

    public int getLibrosConDescargas() {
        return librosConDescargas;
    }

    public void setLibrosConDescargas(int librosConDescargas) {
        this.librosConDescargas = librosConDescargas;
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

    public LibroEntity getLibroMasDescargado() {
        return libroMasDescargado;
    }

    public void setLibroMasDescargado(LibroEntity libroMasDescargado) {
        this.libroMasDescargado = libroMasDescargado;
    }

    public List<String> getIdiomasUnicos() {
        return idiomasUnicos;
    }

    public void setIdiomasUnicos(List<String> idiomasUnicos) {
        this.idiomasUnicos = idiomasUnicos;
    }

    public List<String> getAutoresUnicos() {
        return autoresUnicos;
    }

    public void setAutoresUnicos(List<String> autoresUnicos) {
        this.autoresUnicos = autoresUnicos;
    }

    @Override
    public String toString() {
        return String.format("""
                📊 === ESTADÍSTICAS DETALLADAS ===
                📚 Total de libros: %d
                👤 Libros con autor: %d
                📥 Libros con info de descargas: %d
                📈 Total de descargas: %d
                📊 Promedio de descargas: %.2f
                🏆 Libro más descargado: %s
                🌍 Idiomas únicos: %d
                ✍️ Autores únicos: %d
                ===================================
                """,
                totalLibros,
                librosConAutor,
                librosConDescargas,
                totalDescargas,
                promedioDescargas,
                libroMasDescargado != null ? libroMasDescargado.getTitulo() : "N/A",
                idiomasUnicos != null ? idiomasUnicos.size() : 0,
                autoresUnicos != null ? autoresUnicos.size() : 0);
    }
}
