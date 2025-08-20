package com.alura.literalura.converter;

import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.entity.LibroEntity;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibroConverter {

    @Autowired
    private AutorConverter autorConverter;

    /**
     * Convertir de DTO a Entity (simplificado para un idioma)
     */
    public LibroEntity convertirAEntity(Libro libroDto) {
        if (libroDto == null) {
            return null;
        }

        LibroEntity entity = new LibroEntity();
        entity.setId(libroDto.getId());
        entity.setTitulo(libroDto.getTitulo());
        entity.setNumeroDescargas(libroDto.getNumeroDescargas());

        // Tomar solo el primer idioma
        if (libroDto.getIdiomas() != null && !libroDto.getIdiomas().isEmpty()) {
            entity.setIdioma(libroDto.getIdiomas().get(0));
        }

        // Convertir primer autor
        if (libroDto.getAutores() != null && !libroDto.getAutores().isEmpty()) {
            Autor primerAutor = libroDto.getAutores().get(0);
            AutorEntity autorEntity = autorConverter.convertirAEntity(primerAutor);
            entity.setAutor(autorEntity);
        }

        return entity;
    }

    /**
     * Crear entity con validaciones
     */
    public LibroEntity crearEntityConValidacion(Libro libroDto) {
        if (libroDto == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo");
        }

        if (libroDto.getId() == null) {
            throw new IllegalArgumentException("El libro debe tener un ID válido");
        }

        if (libroDto.getTitulo() == null || libroDto.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El libro debe tener un título válido");
        }

        LibroEntity entity = convertirAEntity(libroDto);

        // Validaciones adicionales
        if (entity.getNumeroDescargas() != null && entity.getNumeroDescargas() < 0) {
            entity.setNumeroDescargas(0);
        }

        // Limpiar título
        if (entity.getTitulo() != null) {
            entity.setTitulo(entity.getTitulo().trim());
        }

        return entity;
    }
}
