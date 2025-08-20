package com.alura.literalura.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class LibroEntity {

    @Id
    private Long id; // ID de Gutendx (no auto-generado)

    @Column(nullable = false, length = 1000)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "autor_id", nullable = false)
    private AutorEntity autor;

    @Column(length = 10)
    private String idioma; // Solo un idioma (primer idioma de la lista)

    @Column(name = "numero_descargas")
    private Integer numeroDescargas;

    @Column(name = "url_descarga", length = 1000)
    private String urlDescarga;

    // Constructores
    public LibroEntity() {
    }

    public LibroEntity(Long id, String titulo, AutorEntity autor, String idioma, Integer numeroDescargas) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
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

    public AutorEntity getAutor() {
        return autor;
    }

    public void setAutor(AutorEntity autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }

    // Métodos de utilidad
    public String getNombreAutor() {
        return autor != null ? autor.getNombre() : "Autor desconocido";
    }

    public String getNombreIdioma() {
        return switch (idioma != null ? idioma.toLowerCase() : "") {
            case "es" -> "Español";
            case "en" -> "Inglés";
            case "fr" -> "Francés";
            case "pt" -> "Portugués";
            case "it" -> "Italiano";
            case "de" -> "Alemán";
            default -> idioma != null ? idioma.toUpperCase() : "Desconocido";
        };
    }

    @Override
    public String toString() {
        return String.format("""
                ═══════════════════════════════════════
                📖 ID: %d
                📚 Título: %s
                👤 Autor: %s
                🌍 Idioma: %s
                📥 Descargas: %s
                ═══════════════════════════════════════
                """,
                id,
                titulo,
                getNombreAutor(),
                getNombreIdioma(),
                numeroDescargas != null ? String.format("%,d", numeroDescargas) : "No disponible");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        LibroEntity libro = (LibroEntity) obj;
        return id != null && id.equals(libro.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
