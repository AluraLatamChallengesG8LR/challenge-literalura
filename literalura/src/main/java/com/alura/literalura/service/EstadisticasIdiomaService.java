package com.alura.literalura.service;

import com.alura.literalura.entity.LibroEntity;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticasIdiomaService {

    @Autowired
    private LibroRepository libroRepository;

    /**
     * FUNCIONALIDAD OBLIGATORIA: Exhibir cantidad de libros en un determinado
     * idioma
     */
    public void exhibirCantidadLibrosPorIdioma(String idioma) {
        System.out.println("📊 === ESTADÍSTICAS PARA IDIOMA: " + obtenerNombreIdioma(idioma).toUpperCase() + " ===");

        // Contar libros usando derived query
        Long cantidadLibros = libroRepository.countByIdioma(idioma.toLowerCase());

        if (cantidadLibros == 0) {
            System.out.println("📭 No hay libros registrados en " + obtenerNombreIdioma(idioma));
            return;
        }

        // Obtener libros del idioma
        List<LibroEntity> libros = libroRepository.findByIdiomaOrderByNumeroDescargasDesc(idioma.toLowerCase());

        // Usar Streams para calcular estadísticas
        var estadisticas = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToInt(LibroEntity::getNumeroDescargas)
                .summaryStatistics();

        // Mostrar estadísticas
        System.out.println("📚 Total de libros: " + cantidadLibros);
        System.out.println("📥 Total de descargas: " + String.format("%,d", estadisticas.getSum()));
        System.out.println("📊 Promedio de descargas: " + String.format("%.2f", estadisticas.getAverage()));
        System.out.println("🏆 Máximo de descargas: " + String.format("%,d", estadisticas.getMax()));
        System.out.println("📉 Mínimo de descargas: " + String.format("%,d", estadisticas.getMin()));

        // Mostrar top 5 libros más descargados en este idioma
        System.out.println("\n🏆 Top 5 libros más descargados en " + obtenerNombreIdioma(idioma) + ":");
        libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .limit(5)
                .forEach(libro -> System.out.printf("   📖 %s - %s descargas%n",
                        libro.getTitulo(),
                        String.format("%,d", libro.getNumeroDescargas())));

        System.out.println("═".repeat(60));
    }

    /**
     * Mostrar estadísticas completas de todos los idiomas
     */
    public void mostrarEstadisticasCompletasIdiomas() {
        System.out.println("🌍 === ESTADÍSTICAS COMPLETAS POR IDIOMA ===");

        // Obtener estadísticas usando consulta personalizada
        List<Object[]> estadisticasDB = libroRepository.getEstadisticasDescargasPorIdioma();

        if (estadisticasDB.isEmpty()) {
            System.out.println("📭 No hay datos de libros para mostrar estadísticas");
            return;
        }

        System.out.println("┌─────────────┬─────────┬──────────────┬─────────────┬─────────────┐");
        System.out.println("│   Idioma    │ Libros  │ Total Desc.  │ Promedio    │ Máximo      │");
        System.out.println("├─────────────┼─────────┼──────────────┼─────────────┼─────────────┤");

        for (Object[] stat : estadisticasDB) {
            String idioma = (String) stat[0];
            Long cantidad = (Long) stat[1];
            Long totalDescargas = stat[2] != null ? ((Number) stat[2]).longValue() : 0L;
            Double promedio = stat[3] != null ? ((Number) stat[3]).doubleValue() : 0.0;
            Integer maximo = stat[4] != null ? ((Number) stat[4]).intValue() : 0;

            System.out.printf("│ %-11s │ %7d │ %12s │ %11.0f │ %11s │%n",
                    obtenerNombreIdioma(idioma),
                    cantidad,
                    String.format("%,d", totalDescargas),
                    promedio,
                    String.format("%,d", maximo));
        }

        System.out.println("└─────────────┴─────────┴──────────────┴─────────────┴─────────────┘");
    }

    /**
     * Listar libros por idioma específico con Streams
     */
    public void listarLibrosPorIdiomaConStreams(String idioma) {
        System.out.println("📚 === LIBROS EN " + obtenerNombreIdioma(idioma).toUpperCase() + " ===");

        List<LibroEntity> libros = libroRepository.findByIdiomaOrderByNumeroDescargasDesc(idioma.toLowerCase());

        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros en " + obtenerNombreIdioma(idioma));
            return;
        }

        // Usar Streams para procesar y mostrar libros
        System.out.println("📋 Lista ordenada por número de descargas:");
        System.out.println("═".repeat(80));

        libros.stream()
                .forEach(libro -> {
                    System.out.printf("📖 %s%n", libro.getTitulo());
                    System.out.printf("   👤 %s%n", libro.getNombreAutor());
                    System.out.printf("   📥 %s descargas%n",
                            libro.getNumeroDescargas() != null ? String.format("%,d", libro.getNumeroDescargas())
                                    : "N/A");
                    System.out.println("   " + "─".repeat(70));
                });

        // Estadísticas usando Streams
        long totalLibros = libros.size();
        long librosConDescargas = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .count();

        System.out.printf("📊 Total: %d libros | Con datos de descarga: %d%n",
                totalLibros, librosConDescargas);
    }

    /**
     * Comparar estadísticas entre dos idiomas
     */
    public void compararIdiomas(String idioma1, String idioma2) {
        System.out.println("⚖️ === COMPARACIÓN ENTRE IDIOMAS ===");
        System.out.printf("🆚 %s vs %s%n",
                obtenerNombreIdioma(idioma1),
                obtenerNombreIdioma(idioma2));
        System.out.println("═".repeat(50));

        // Obtener datos de ambos idiomas
        Long cantidad1 = libroRepository.countByIdioma(idioma1.toLowerCase());
        Long cantidad2 = libroRepository.countByIdioma(idioma2.toLowerCase());

        List<LibroEntity> libros1 = libroRepository.findByIdiomaOrderByNumeroDescargasDesc(idioma1.toLowerCase());
        List<LibroEntity> libros2 = libroRepository.findByIdiomaOrderByNumeroDescargasDesc(idioma2.toLowerCase());

        // Calcular estadísticas con Streams
        var stats1 = libros1.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToInt(LibroEntity::getNumeroDescargas)
                .summaryStatistics();

        var stats2 = libros2.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToInt(LibroEntity::getNumeroDescargas)
                .summaryStatistics();

        // Mostrar comparación
        System.out.printf("📚 Cantidad de libros:%n");
        System.out.printf("   %s: %d libros%n", obtenerNombreIdioma(idioma1), cantidad1);
        System.out.printf("   %s: %d libros%n", obtenerNombreIdioma(idioma2), cantidad2);

        System.out.printf("📥 Promedio de descargas:%n");
        System.out.printf("   %s: %.0f descargas%n", obtenerNombreIdioma(idioma1), stats1.getAverage());
        System.out.printf("   %s: %.0f descargas%n", obtenerNombreIdioma(idioma2), stats2.getAverage());

        // Determinar ganador
        String ganador = cantidad1 > cantidad2 ? obtenerNombreIdioma(idioma1) : obtenerNombreIdioma(idioma2);
        System.out.printf("🏆 Idioma con más libros: %s%n", ganador);

        System.out.println("═".repeat(50));
    }

    /**
     * Obtener idiomas disponibles en la base de datos
     */
    public List<String> obtenerIdiomasDisponibles() {
        return libroRepository.findIdiomasDisponibles();
    }

    /**
     * Mostrar distribución de libros por idioma usando Streams
     */
    public void mostrarDistribucionIdiomas() {
        System.out.println("📊 === DISTRIBUCIÓN DE LIBROS POR IDIOMA ===");

        // Obtener todos los libros y agrupar por idioma usando Streams
        List<LibroEntity> todosLosLibros = libroRepository.findAll();

        Map<String, Long> distribucion = todosLosLibros.stream()
                .collect(Collectors.groupingBy(
                        LibroEntity::getIdioma,
                        Collectors.counting()));

        // Mostrar distribución ordenada por cantidad
        distribucion.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    String idioma = entry.getKey();
                    Long cantidad = entry.getValue();
                    double porcentaje = (cantidad * 100.0) / todosLosLibros.size();

                    System.out.printf("🌍 %-15s: %3d libros (%5.1f%%)%n",
                            obtenerNombreIdioma(idioma),
                            cantidad,
                            porcentaje);
                });

        System.out.printf("📊 Total de libros: %d%n", todosLosLibros.size());
        System.out.printf("🗣️ Total de idiomas: %d%n", distribucion.size());
        System.out.println("═".repeat(50));
    }

    /**
     * Obtener nombre completo del idioma
     */
    private String obtenerNombreIdioma(String codigo) {
        if (codigo == null)
            return "Desconocido";

        return switch (codigo.toLowerCase()) {
            case "es" -> "Español";
            case "en" -> "Inglés";
            case "fr" -> "Francés";
            case "pt" -> "Portugués";
            case "it" -> "Italiano";
            case "de" -> "Alemán";
            case "ru" -> "Ruso";
            case "zh" -> "Chino";
            case "ja" -> "Japonés";
            case "ar" -> "Árabe";
            default -> codigo.toUpperCase();
        };
    }
}
