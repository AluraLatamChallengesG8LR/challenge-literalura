package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {

    @JsonAlias("id")
    private Long id;

    @JsonAlias("title")
    private String titulo;

    @JsonAlias("authors")
    private List<Autor> autores;

    @JsonAlias("languages")
    private List<String> idiomas;

    @JsonAlias("subjects")
    private List<String> temas;

    @JsonAlias("download_count")
    private Integer numeroDescargas;

    @JsonAlias("formats")
    private Object formatos; // Para enlaces de descarga

    // Constructores
    public Libro() {
    }

    public Libro(Long id, String titulo, List<Autor> autores, List<String> idiomas, Integer numeroDescargas) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = idiomas;
        this.numeroDescargas = numeroDescargas;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public List<String> getTemas() {
        return temas;
    }

    public void setTemas(List<String> temas) {
        this.temas = temas;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Object getFormatos() {
        return formatos;
    }

    public void setFormatos(Object formatos) {
        this.formatos = formatos;
    }

    // Métodos de utilidad
    public String getPrimerAutor() {
        return autores != null && !autores.isEmpty() ? autores.get(0).getNombre() : "Autor desconocido";
    }

    public String getPrimerIdioma() {
        return idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "Idioma desconocido";
    }

    @Override
    public String toString() {
        return String.format("""
                📖 Libro ID: %d
                📚 Título: %s
                👤 Autor(es): %s
                🌍 Idioma(s): %s
                📥 Descargas: %s
                """,
                id,
                titulo,
                autores != null ? autores.toString() : "No disponible",
                idiomas != null ? String.join(", ", idiomas) : "No disponible",
                numeroDescargas != null ? numeroDescargas : "No disponible");
    }
}
