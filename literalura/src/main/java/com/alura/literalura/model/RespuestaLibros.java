package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaLibros {

    @JsonAlias("count")
    private Integer totalResultados;

    @JsonAlias("next")
    private String siguientePagina;

    @JsonAlias("previous")
    private String paginaAnterior;

    @JsonAlias("results")
    private List<Libro> libros;

    // Constructores
    public RespuestaLibros() {
    }

    // Getters y Setters
    public Integer getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Integer totalResultados) {
        this.totalResultados = totalResultados;
    }

    public String getSiguientePagina() {
        return siguientePagina;
    }

    public void setSiguientePagina(String siguientePagina) {
        this.siguientePagina = siguientePagina;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    // Métodos de utilidad
    public boolean tieneSiguientePagina() {
        return siguientePagina != null && !siguientePagina.isEmpty();
    }

    public boolean tienePaginaAnterior() {
        return paginaAnterior != null && !paginaAnterior.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("""
                📊 Total de resultados: %d
                📄 Libros en esta página: %d
                ➡️ Siguiente página: %s
                ⬅️ Página anterior: %s
                """,
                totalResultados,
                libros != null ? libros.size() : 0,
                tieneSiguientePagina() ? "Sí" : "No",
                tienePaginaAnterior() ? "Sí" : "No");
    }
}
