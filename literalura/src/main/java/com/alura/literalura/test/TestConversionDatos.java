package com.alura.literalura.test;

import com.alura.literalura.entity.LibroEntity;
import com.alura.literalura.service.GutendxServiceMejorado;
import com.alura.literalura.service.ProcesadorDatos;
import com.alura.literalura.util.CriteriosFiltro;
import com.alura.literalura.util.EstadisticasDetalladas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TestConversionDatos implements CommandLineRunner {

    @Autowired
    private GutendxServiceMejorado gutendxService;

    @Autowired
    private ProcesadorDatos procesador;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando pruebas de conversión de datos...\n");

        try {
            // Prueba 1: Procesar respuesta completa
            probarProcesamientoCompleto();

            // Prueba 2: Agrupar por autor
            probarAgrupacionPorAutor();

            // Prueba 3: Filtrar libros
            probarFiltradoLibros();

            // Prueba 4: Estadísticas detalladas
            probarEstadisticasDetalladas();

        } catch (Exception e) {
            System.err.println("❌ Error en las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void probarProcesamientoCompleto() throws Exception {
        System.out.println("=== PRUEBA 1: Procesamiento completo de datos ===");

        // Buscar libros de Shakespeare
        var respuesta = gutendxService.buscarLibros("Shakespeare");

        // Procesar los primeros 5 libros para evitar sobrecarga
        var librosParaProcesar = respuesta.getLibros().subList(0, Math.min(5, respuesta.getLibros().size()));

        for (var libro : librosParaProcesar) {
            LibroEntity libroEntity = procesador.procesarLibroIndividual(libro);
            if (libroEntity != null) {
                System.out.println("✅ Libro convertido:");
                System.out.println(libroEntity);
            }
        }
        System.out.println();
    }

    private void probarAgrupacionPorAutor() throws Exception {
        System.out.println("=== PRUEBA 2: Agrupación por autor ===");

        var respuesta = gutendxService.buscarLibros("Cervantes");
        List<LibroEntity> librosEntity = procesador.convertirListaAEntity(respuesta.getLibros());

        Map<String, List<LibroEntity>> librosPorAutor = procesador.agruparLibrosPorAutor(librosEntity);

        System.out.println("📊 Libros agrupados por autor:");
        librosPorAutor.forEach((autor, libros) -> {
            System.out.printf("👤 %s: %d libros%n", autor, libros.size());
            libros.forEach(libro -> System.out.printf("   📖 %s%n", libro.getTitulo()));
        });
        System.out.println();
    }

    private void probarFiltradoLibros() throws Exception {
        System.out.println("=== PRUEBA 3: Filtrado de libros ===");

        var respuesta = gutendxService.buscarPorIdioma("es");
        List<LibroEntity> librosEntity = procesador.convertirListaAEntity(respuesta.getLibros());

        // Filtrar libros en español con más de 100 descargas
        CriteriosFiltro criterios = new CriteriosFiltro();
        criterios.setIdioma("es");
        criterios.setDescargasMinimas(100);

        List<LibroEntity> librosFiltrados = procesador.filtrarLibros(librosEntity, criterios);

        System.out.println("🔍 Filtros aplicados: " + criterios);
        System.out.println("📚 Libros que cumplen los criterios:");

        librosFiltrados.stream()
                .limit(5)
                .forEach(libro -> {
                    System.out.printf("📖 %s (%s descargas)%n",
                            libro.getTitulo(),
                            libro.getNumeroDescargas());
                });
        System.out.println();
    }

    private void probarEstadisticasDetalladas() throws Exception {
        System.out.println("=== PRUEBA 4: Estadísticas detalladas ===");

        var respuesta = gutendxService.buscarLibros("Don Quixote");
        List<LibroEntity> librosEntity = procesador.convertirListaAEntity(respuesta.getLibros());

        EstadisticasDetalladas stats = procesador.obtenerEstadisticas(librosEntity);
        System.out.println(stats);
    }
}
