package com.alura.literalura.test;

import com.alura.literalura.model.Libro;
import com.alura.literalura.model.RespuestaLibros;
import com.alura.literalura.service.GutendxServiceMejorado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestJacksonConversion implements CommandLineRunner {

    @Autowired
    private GutendxServiceMejorado gutendxService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando pruebas con Jackson para conversión JSON...\n");

        try {
            // Prueba 1: Buscar libros y mostrar objetos Java
            probarBusquedaLibros();

            // Prueba 2: Obtener libro específico
            probarLibroEspecifico();

            // Prueba 3: Buscar por idioma
            probarBusquedaPorIdioma();

            // Prueba 4: Análisis detallado de datos
            probarAnalisisDetallado();

        } catch (Exception e) {
            System.err.println("❌ Error en las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void probarBusquedaLibros() throws Exception {
        System.out.println("=== PRUEBA 1: Búsqueda de libros con Jackson ===");

        RespuestaLibros respuesta = gutendxService.buscarLibros("Don Quixote");

        System.out.println("📊 Información de la respuesta:");
        System.out.println(respuesta);

        System.out.println("📚 Primeros 3 libros encontrados:");
        List<Libro> libros = respuesta.getLibros();

        for (int i = 0; i < Math.min(3, libros.size()); i++) {
            Libro libro = libros.get(i);
            System.out.println("--- Libro " + (i + 1) + " ---");
            System.out.println(libro);
        }
        System.out.println();
    }

    private void probarLibroEspecifico() throws Exception {
        System.out.println("=== PRUEBA 2: Libro específico con Jackson ===");

        Libro libro = gutendxService.obtenerLibroPorId(1L);

        System.out.println("📖 Información detallada del libro:");
        System.out.println(libro);

        System.out.println("🔍 Análisis de campos específicos:");
        System.out.println("- Primer autor: " + libro.getPrimerAutor());
        System.out.println("- Primer idioma: " + libro.getPrimerIdioma());
        System.out.println("- Total de autores: " +
                (libro.getAutores() != null ? libro.getAutores().size() : 0));
        System.out.println("- Total de idiomas: " +
                (libro.getIdiomas() != null ? libro.getIdiomas().size() : 0));
        System.out.println();
    }

    private void probarBusquedaPorIdioma() throws Exception {
        System.out.println("=== PRUEBA 3: Búsqueda por idioma con Jackson ===");

        RespuestaLibros respuesta = gutendxService.buscarPorIdioma("es");

        System.out.println("🌍 Libros en español encontrados:");
        System.out.println(respuesta);

        System.out.println("📚 Primeros 2 libros en español:");
        List<Libro> libros = respuesta.getLibros();

        for (int i = 0; i < Math.min(2, libros.size()); i++) {
            Libro libro = libros.get(i);
            System.out.println("--- Libro en español " + (i + 1) + " ---");
            System.out.printf("📖 %s por %s%n",
                    libro.getTitulo(),
                    libro.getPrimerAutor());
            System.out.printf("📥 Descargas: %s%n",
                    libro.getNumeroDescargas() != null ? libro.getNumeroDescargas() : "No disponible");
        }
        System.out.println();
    }

    private void probarAnalisisDetallado() throws Exception {
        System.out.println("=== PRUEBA 4: Análisis detallado de datos ===");

        List<Libro> librosShakespeare = gutendxService.buscarPorAutor("Shakespeare");

        System.out.println("🎭 Análisis de libros de Shakespeare:");
        System.out.println("Total de libros encontrados: " + librosShakespeare.size());

        // Estadísticas
        int totalDescargas = librosShakespeare.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .mapToInt(Libro::getNumeroDescargas)
                .sum();

        System.out.println("📥 Total de descargas: " + totalDescargas);

        // Libro más descargado
        Libro masDescargado = librosShakespeare.stream()
                .filter(libro -> libro.getNumeroDescargas() != null)
                .max((l1, l2) -> Integer.compare(l1.getNumeroDescargas(), l2.getNumeroDescargas()))
                .orElse(null);

        if (masDescargado != null) {
            System.out.println("🏆 Libro más descargado:");
            System.out.printf("   📖 %s (%d descargas)%n",
                    masDescargado.getTitulo(),
                    masDescargado.getNumeroDescargas());
        }

        System.out.println("✅ Análisis completado!\n");
    }
}
