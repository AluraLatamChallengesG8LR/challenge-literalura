package com.alura.literalura.service;

import com.alura.literalura.converter.AutorConverter;
import com.alura.literalura.converter.LibroConverter;
import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.entity.LibroEntity;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.RespuestaLibros;
import com.alura.literalura.util.CriteriosFiltro;
import com.alura.literalura.util.EstadisticasDetalladas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcesadorDatos {

    @Autowired
    private LibroConverter libroConverter;

    @Autowired
    private AutorConverter autorConverter;

    @Autowired
    private ConvierteDatos conversor;

    /**
     * Procesar respuesta JSON completa de la API
     */
    public List<LibroEntity> procesarRespuestaAPI(String jsonRespuesta) {
        System.out.println("🔄 Iniciando procesamiento de datos JSON...");

        try {
            // Convertir JSON a objeto Java
            RespuestaLibros respuesta = conversor.obtenerDatosConLog(jsonRespuesta, RespuestaLibros.class);

            System.out.println("📊 Respuesta procesada: " + respuesta.getTotalResultados() + " libros encontrados");

            // Convertir libros DTO a entities
            List<LibroEntity> librosEntity = new ArrayList<>();

            if (respuesta.getLibros() != null) {
                for (Libro libroDto : respuesta.getLibros()) {
                    try {
                        LibroEntity libroEntity = procesarLibroIndividual(libroDto);
                        if (libroEntity != null) {
                            librosEntity.add(libroEntity);
                        }
                    } catch (Exception e) {
                        System.err.println("⚠️ Error procesando libro ID " + libroDto.getId() + ": " + e.getMessage());
                    }
                }
            }

            System.out.println("✅ Procesamiento completado: " + librosEntity.size() + " libros convertidos");
            return librosEntity;

        } catch (Exception e) {
            System.err.println("❌ Error en procesamiento de datos: " + e.getMessage());
            throw new RuntimeException("Error al procesar respuesta de la API", e);
        }
    }

    /**
     * Procesar un libro individual con validaciones
     */
    public LibroEntity procesarLibroIndividual(Libro libroDto) {
        if (libroDto == null) {
            System.out.println("⚠️ Libro nulo encontrado, omitiendo...");
            return null;
        }

        System.out.println("📖 Procesando: " + libroDto.getTitulo());

        try {
            // Validar datos básicos
            validarDatosLibro(libroDto);

            // Convertir a entity
            LibroEntity libroEntity = libroConverter.crearEntityConValidacion(libroDto);

            // Procesar y limpiar datos
            limpiarDatosLibro(libroEntity);

            System.out.println("✅ Libro procesado correctamente: " + libroEntity.getTitulo());
            return libroEntity;

        } catch (Exception e) {
            System.err.println("❌ Error procesando libro: " + e.getMessage());
            return null;
        }
    }

    /**
     * Agrupar libros por autor
     */
    public Map<String, List<LibroEntity>> agruparLibrosPorAutor(List<LibroEntity> libros) {
        System.out.println("📊 Agrupando " + libros.size() + " libros por autor...");

        Map<String, List<LibroEntity>> librosPorAutor = libros.stream()
                .filter(libro -> libro.getAutor() != null)
                .collect(Collectors.groupingBy(
                        libro -> libro.getAutor().getNombre(),
                        Collectors.toList()));

        System.out.println("👥 Agrupación completada: " + librosPorAutor.size() + " autores únicos");
        return librosPorAutor;
    }

    /**
     * Filtrar libros por criterios específicos
     */
    public List<LibroEntity> filtrarLibros(List<LibroEntity> libros, CriteriosFiltro criterios) {
        System.out.println("🔍 Aplicando filtros a " + libros.size() + " libros...");

        List<LibroEntity> librosFiltrados = libros.stream()
                .filter(libro -> aplicarFiltros(libro, criterios))
                .collect(Collectors.toList());

        System.out.println("✅ Filtrado completado: " + librosFiltrados.size() + " libros coinciden");
        return librosFiltrados;
    }

    /**
     * Obtener estadísticas detalladas
     */
    public EstadisticasDetalladas obtenerEstadisticas(List<LibroEntity> libros) {
        System.out.println("📈 Calculando estadísticas para " + libros.size() + " libros...");

        EstadisticasDetalladas stats = new EstadisticasDetalladas();

        // Contadores básicos
        stats.setTotalLibros(libros.size());
        stats.setLibrosConAutor((int) libros.stream().filter(l -> l.getAutor() != null).count());
        stats.setLibrosConDescargas((int) libros.stream().filter(l -> l.getNumeroDescargas() != null).count());

        // Estadísticas de descargas
        if (!libros.isEmpty()) {
            int totalDescargas = libros.stream()
                    .filter(l -> l.getNumeroDescargas() != null)
                    .mapToInt(LibroEntity::getNumeroDescargas)
                    .sum();

            double promedioDescargas = libros.stream()
                    .filter(l -> l.getNumeroDescargas() != null)
                    .mapToInt(LibroEntity::getNumeroDescargas)
                    .average()
                    .orElse(0.0);

            LibroEntity libroMasDescargado = libros.stream()
                    .filter(l -> l.getNumeroDescargas() != null)
                    .max((l1, l2) -> Integer.compare(l1.getNumeroDescargas(), l2.getNumeroDescargas()))
                    .orElse(null);

            stats.setTotalDescargas(totalDescargas);
            stats.setPromedioDescargas(promedioDescargas);
            stats.setLibroMasDescargado(libroMasDescargado);
        }

        // Autores únicos
        List<String> autoresUnicos = libros.stream()
                .filter(l -> l.getAutor() != null)
                .map(l -> l.getAutor().getNombre())
                .distinct()
                .collect(Collectors.toList());
        stats.setAutoresUnicos(autoresUnicos);

        System.out.println("📊 Estadísticas calculadas exitosamente");
        return stats;
    }

    /**
     * Validar datos del libro
     */
    private void validarDatosLibro(Libro libro) {
        if (libro.getId() == null) {
            throw new IllegalArgumentException("Libro sin ID válido");
        }

        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("Libro sin título válido");
        }

        if (libro.getAutores() == null || libro.getAutores().isEmpty()) {
            System.out.println("⚠️ Libro sin autores: " + libro.getTitulo());
        }
    }

    /**
     * Limpiar y normalizar datos del libro
     */
    private void limpiarDatosLibro(LibroEntity libro) {
        // Limpiar título
        if (libro.getTitulo() != null) {
            libro.setTitulo(libro.getTitulo().trim());
        }

        // Validar número de descargas
        if (libro.getNumeroDescargas() != null && libro.getNumeroDescargas() < 0) {
            libro.setNumeroDescargas(0);
        }
    }

    /**
     * Aplicar filtros individuales
     */
    private boolean aplicarFiltros(LibroEntity libro, CriteriosFiltro criterios) {
        if (criterios == null) {
            return true;
        }

        // Filtro por autor
        if (criterios.getAutor() != null) {
            if (libro.getAutor() == null ||
                    !libro.getAutor().getNombre().toLowerCase().contains(criterios.getAutor().toLowerCase())) {
                return false;
            }
        }

        // Filtro por título
        if (criterios.getTitulo() != null) {
            if (!libro.getTitulo().toLowerCase().contains(criterios.getTitulo().toLowerCase())) {
                return false;
            }
        }

        // Filtro por número mínimo de descargas
        if (criterios.getDescargasMinimas() != null) {
            if (libro.getNumeroDescargas() == null || libro.getNumeroDescargas() < criterios.getDescargasMinimas()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convertir lista de DTOs a Entities (método delegado)
     */
    public List<LibroEntity> convertirListaAEntity(List<Libro> librosDto) {
        if (librosDto == null) {
            return new ArrayList<>();
        }

        System.out.println("🔄 Convirtiendo " + librosDto.size() + " libros DTO a Entity...");

        List<LibroEntity> librosEntity = new ArrayList<>();

        for (Libro libroDto : librosDto) {
            try {
                LibroEntity libroEntity = procesarLibroIndividual(libroDto);
                if (libroEntity != null) {
                    librosEntity.add(libroEntity);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error convirtiendo libro ID " + libroDto.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Conversión completada: " + librosEntity.size() + " libros convertidos");
        return librosEntity;
    }

}
