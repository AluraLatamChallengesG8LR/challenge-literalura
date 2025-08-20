package com.alura.literalura.converter;

import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.model.Autor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AutorConverter {

    /**
     * Convertir de DTO a Entity
     */
    public AutorEntity convertirAEntity(Autor autorDto) {
        if (autorDto == null) {
            return null;
        }

        AutorEntity entity = new AutorEntity();
        entity.setNombre(autorDto.getNombre());
        entity.setAnoNacimiento(autorDto.getAnoNacimiento());
        entity.setAnoFallecimiento(autorDto.getAnoFallecimiento());

        return entity;
    }

    /**
     * Convertir de Entity a DTO
     */
    public Autor convertirADto(AutorEntity autorEntity) {
        if (autorEntity == null) {
            return null;
        }

        Autor dto = new Autor();
        dto.setNombre(autorEntity.getNombre());
        dto.setAnoNacimiento(autorEntity.getAnoNacimiento());
        dto.setAnoFallecimiento(autorEntity.getAnoFallecimiento());

        return dto;
    }

    /**
     * Convertir lista de DTOs a Entities
     */
    public List<AutorEntity> convertirListaAEntity(List<Autor> autoresDto) {
        if (autoresDto == null) {
            return null;
        }

        return autoresDto.stream()
                .map(this::convertirAEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convertir lista de Entities a DTOs
     */
    public List<Autor> convertirListaADto(List<AutorEntity> autoresEntity) {
        if (autoresEntity == null) {
            return null;
        }

        return autoresEntity.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar entity existente con datos del DTO
     */
    public void actualizarEntity(AutorEntity entity, Autor dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getNombre() != null) {
            entity.setNombre(dto.getNombre());
        }
        if (dto.getAnoNacimiento() != null) {
            entity.setAnoNacimiento(dto.getAnoNacimiento());
        }
        if (dto.getAnoFallecimiento() != null) {
            entity.setAnoFallecimiento(dto.getAnoFallecimiento());
        }
    }

    /**
     * Crear entity con validaciones
     */
    public AutorEntity crearEntityConValidacion(Autor autorDto) {
        if (autorDto == null || autorDto.getNombre() == null || autorDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor debe tener un nombre válido");
        }

        AutorEntity entity = convertirAEntity(autorDto);

        // Validaciones adicionales
        if (entity.getAnoNacimiento() != null && entity.getAnoFallecimiento() != null) {
            if (entity.getAnoNacimiento() > entity.getAnoFallecimiento()) {
                throw new IllegalArgumentException("El año de nacimiento no puede ser mayor al año de fallecimiento");
            }
        }

        return entity;
    }
}
