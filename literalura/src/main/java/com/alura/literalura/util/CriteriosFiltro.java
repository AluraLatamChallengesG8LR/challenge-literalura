package com.alura.literalura.util;

public class CriteriosFiltro {
    private String idioma;
    private String autor;
    private String titulo;
    private Integer descargasMinimas;

    // Constructores
    public CriteriosFiltro() {
    }

    public CriteriosFiltro(String idioma, String autor, String titulo, Integer descargasMinimas) {
        this.idioma = idioma;
        this.autor = autor;
        this.titulo = titulo;
        this.descargasMinimas = descargasMinimas;
    }

    // Getters y Setters
    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDescargasMinimas() {
        return descargasMinimas;
    }

    public void setDescargasMinimas(Integer descargasMinimas) {
        this.descargasMinimas = descargasMinimas;
    }

    // Métodos de utilidad
    public static CriteriosFiltro porIdioma(String idioma) {
        CriteriosFiltro criterios = new CriteriosFiltro();
        criterios.setIdioma(idioma);
        return criterios;
    }

    public static CriteriosFiltro porAutor(String autor) {
        CriteriosFiltro criterios = new CriteriosFiltro();
        criterios.setAutor(autor);
        return criterios;
    }

    public static CriteriosFiltro porTitulo(String titulo) {
        CriteriosFiltro criterios = new CriteriosFiltro();
        criterios.setTitulo(titulo);
        return criterios;
    }

    public static CriteriosFiltro porDescargas(Integer minimas) {
        CriteriosFiltro criterios = new CriteriosFiltro();
        criterios.setDescargasMinimas(minimas);
        return criterios;
    }

    @Override
    public String toString() {
        return String.format("Filtros: idioma=%s, autor=%s, titulo=%s, descargas>=%d",
                idioma, autor, titulo, descargasMinimas);
    }
}
