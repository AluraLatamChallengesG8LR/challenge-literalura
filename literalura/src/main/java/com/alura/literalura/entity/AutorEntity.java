package com.alura.literalura.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class AutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500, unique = true)
    private String nombre;

    @Column(name = "ano_nacimiento")
    private Integer anoNacimiento;

    @Column(name = "ano_fallecimiento")
    private Integer anoFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LibroEntity> libros = new ArrayList<>();

    // Constructores
    public AutorEntity() {
    }

    public AutorEntity(String nombre, Integer anoNacimiento, Integer anoFallecimiento) {
        this.nombre = nombre;
        this.anoNacimiento = anoNacimiento;
        this.anoFallecimiento = anoFallecimiento;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnoNacimiento() {
        return anoNacimiento;
    }

    public void setAnoNacimiento(Integer anoNacimiento) {
        this.anoNacimiento = anoNacimiento;
    }

    public Integer getAnoFallecimiento() {
        return anoFallecimiento;
    }

    public void setAnoFallecimiento(Integer anoFallecimiento) {
        this.anoFallecimiento = anoFallecimiento;
    }

    public List<LibroEntity> getLibros() {
        return libros;
    }

    public void setLibros(List<LibroEntity> libros) {
        this.libros = libros;
    }

    // Métodos de utilidad
    public void agregarLibro(LibroEntity libro) {
        libros.add(libro);
        libro.setAutor(this);
    }

    public boolean estaVivo() {
        return anoFallecimiento == null;
    }

    public String getPeriodoVida() {
        String inicio = anoNacimiento != null ? anoNacimiento.toString() : "?";
        String fin = anoFallecimiento != null ? anoFallecimiento.toString() : "presente";
        return inicio + " - " + fin;
    }

    @Override
    public String toString() {
        return String.format("👤 %s (%s) - %d libros",
                nombre,
                getPeriodoVida(),
                libros.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        AutorEntity autor = (AutorEntity) obj;
        return nombre != null && nombre.equals(autor.nombre);
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.hashCode() : 0;
    }
}
