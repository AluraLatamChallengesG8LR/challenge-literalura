package com.alura.literalura.util;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalizadorLibros {

    /**
     * Obtener estadísticas de una lista de libros
     */
    public static EstadisticasLibros analizarLibros(List<Libro> libros) {
        if (libros == null || libros.isEmpty()) {
            return new EstadisticasLibros();
        }

        // Total de libros
        int totalLibros = libros.size();

        // Total de descargas
        int totalDescargas = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToInt(Libro::getNumeroDescargas)
                .sum();

        // Promedio de descargas
        double promedioDescargas = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToInt(Libro::getNumeroDescargas)
                .average()
                .orElse(0.0);

        // Idiomas más comunes
        Map<String, Long> idiomasFrecuencia = libros.stream()
                .filter(libro -> libro.getIdiomas() != null)
                .flatMap(libro -> libro.getIdiomas().stream())
                .collect(Collectors.groupingBy(idioma -> idioma, Collectors.counting()));

        // Autores más comunes
        Map<String, Long> autoresFrecuencia = libros.stream()
                .filter(libro -> libro.getAutores() != null)
                .flatMap(libro -> libro.getAutores().stream())
                .map(Autor::getNombre)
                .collect(Collectors.groupingBy(nombre -> nombre, Collectors.counting()));

        // Libro más descargado
        Libro libroMasDescargado = libros.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .max((l1, l2) -> Integer.compare(l1.getNumeroDescargas(), l2.getNumeroDescargas()))
                .orElse(null);

        return new EstadisticasLibros(
                totalLibros,
                totalDescargas,
                promedioDescargas,
                idiomasFrecuencia,
                autoresFrecuencia,
                libroMasDescargado);
    }

    /**
     * Mostrar estadísticas formateadas
     */
    public static void mostrarEstadisticas(List<Libro> libros, String categoria) {
        EstadisticasLibros stats = analizarLibros(libros);

        System.out.println("📊 === ESTADÍSTICAS: " + categoria.toUpperCase() + " ===");
        System.out.println("📚 Total de libros: " + stats.getTotalLibros());
        System.out.println("📥 Total de descargas: " + stats.getTotalDescargas());
        System.out.println("📈 Promedio de descargas: " + String.format("%.2f", stats.getPromedioDescargas()));

        System.out.println("\n🌍 Top 3 idiomas:");
        stats.getIdiomasFrecuencia().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " libros"));

        System.out.println("\n👤 Top 3 autores:");
        stats.getAutoresFrecuencia().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> System.out.println("   " + entry.getKey() + ": " + entry.getValue() + " libros"));

        if (stats.getLibroMasDescargado() != null) {
            System.out.println("\n🏆 Libro más descargado:");
            System.out.println("   📖 " + stats.getLibroMasDescargado().getTitulo());
            System.out.println("   📥 " + stats.getLibroMasDescargado().getNumeroDescargas() + " descargas");
        }

        System.out.println("═".repeat(50) + "\n");
    }
}
