package com.alura.literalura.principal;

import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.entity.LibroEntity;
import com.alura.literalura.service.AutoresVivosService;
import com.alura.literalura.service.EstadisticasIdiomaService;
import com.alura.literalura.service.PersistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {

    private Scanner teclado = new Scanner(System.in);

    @Autowired
    private PersistenciaService persistenciaService;

    @Autowired
    private EstadisticasIdiomaService estadisticasIdiomaService;

    @Autowired
    private AutoresVivosService autoresVivosService;

    public void muestraElMenu() {
        mostrarBienvenida();

        var opcion = -1;
        while (opcion != 0) {
            var menu = """

                    ═══════════════════════════════════════════
                    📚 LITERALURA - CATÁLOGO DE LIBROS 📚
                    ═══════════════════════════════════════════

                    1️⃣  - Buscar libro por título
                    2️⃣  - Listar libros registrados
                    3️⃣  - Listar autores registrados
                    4️⃣  - Listar autores vivos en un año determinado
                    5️⃣  - Listar libros por idioma
                    6️⃣  - Estadísticas por idioma específico
                    7️⃣  - Estadísticas completas de idiomas
                    8️⃣  - Comparar dos idiomas
                    9️⃣  - Distribución de libros por idioma
                    🔟  - Menú avanzado de autores
                    0️⃣  - Salir

                    ═══════════════════════════════════════════
                    Seleccione una opción: """;

            System.out.print(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1 -> buscarYPersistirLibro();
                    case 2 -> listarLibrosPersistidos();
                    case 3 -> listarAutoresPersistidos();
                    case 4 -> listarAutoresVivosEnAno();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> mostrarEstadisticasPorIdioma();
                    case 7 -> mostrarEstadisticasCompletasIdiomas();
                    case 8 -> compararDosIdiomas();
                    case 9 -> mostrarDistribucionIdiomas();
                    case 10 -> menuAutoresAvanzado();
                    case 0 -> {
                        System.out.println("\n🙏 ¡Gracias por usar LiterAlura!");
                        System.out.println("📚 ¡Sus datos están seguros en PostgreSQL! 📚");
                    }
                    default -> System.out.println("\n❌ Opción no válida. Seleccione del 0 al 10.");
                }

                if (opcion != 0) {
                    esperarEnter();
                }

            } catch (InputMismatchException e) {
                System.out.println("\n❌ Error: Ingrese un número válido.");
                teclado.nextLine();
            } catch (Exception e) {
                System.out.println("\n❌ Error inesperado: " + e.getMessage());
                teclado.nextLine();
            }
        }
    }

    // ===== FUNCIONALIDADES PRINCIPALES =====

    private void buscarYPersistirLibro() {
        System.out.println("\n🔍 === BUSCAR Y GUARDAR LIBRO ===");
        System.out.print("📖 Ingrese el título del libro: ");

        try {
            String titulo = teclado.nextLine().trim();

            if (titulo.isEmpty()) {
                System.out.println("❌ El título no puede estar vacío.");
                return;
            }

            System.out.println("\n🔄 Buscando en API y guardando en PostgreSQL...");
            LibroEntity libro = persistenciaService.buscarYPersistirLibro(titulo);

            if (libro != null) {
                System.out.println("\n✅ ¡LIBRO GUARDADO EN BASE DE DATOS!");
                System.out.println(libro.toString());
            } else {
                System.out.println("\n❌ No se pudo encontrar o guardar el libro.");
                System.out.println("💡 Sugerencias:");
                System.out.println("   • Verifique la ortografía del título");
                System.out.println("   • Intente con palabras clave del título");
                System.out.println("   • Use el título en inglés si es posible");
            }

        } catch (Exception e) {
            System.out.println("\n❌ Error: " + e.getMessage());
        }
    }

    private void listarLibrosPersistidos() {
        System.out.println("\n📚 === LIBROS EN BASE DE DATOS ===");

        try {
            List<LibroEntity> libros = persistenciaService.listarTodosLosLibrosPersistidos();

            if (libros.isEmpty()) {
                System.out.println("📭 No hay libros en la base de datos.");
                System.out.println("💡 Use la opción 1 para buscar y agregar libros.");
                return;
            }

            System.out.println("\n📋 Libros guardados en PostgreSQL:");
            System.out.println("═".repeat(80));

            for (int i = 0; i < libros.size(); i++) {
                LibroEntity libro = libros.get(i);
                System.out.printf("%d. 📖 %s%n", (i + 1), libro.getTitulo());
                System.out.printf("   👤 %s | 🌍 %s | 📥 %s%n",
                        libro.getNombreAutor(),
                        libro.getNombreIdioma(),
                        libro.getNumeroDescargas() != null ? String.format("%,d", libro.getNumeroDescargas()) : "N/A");
                System.out.printf("   🆔 ID: %d%n", libro.getId());
                System.out.println("   " + "─".repeat(70));
            }

            System.out.printf("\n📊 Total: %d libros registrados%n", libros.size());

        } catch (Exception e) {
            System.out.println("❌ Error al consultar base de datos: " + e.getMessage());
        }
    }

    private void listarAutoresPersistidos() {
        System.out.println("\n👥 === AUTORES EN BASE DE DATOS ===");

        try {
            List<AutorEntity> autores = persistenciaService.listarTodosLosAutoresPersistidos();

            if (autores.isEmpty()) {
                System.out.println("📭 No hay autores en la base de datos.");
                System.out.println("💡 Los autores se agregan automáticamente al buscar libros.");
                return;
            }

            System.out.println("\n📋 Autores guardados en PostgreSQL:");
            System.out.println("═".repeat(80));

            for (int i = 0; i < autores.size(); i++) {
                AutorEntity autor = autores.get(i);
                System.out.printf("%d. 👤 %s%n", (i + 1), autor.getNombre());
                System.out.printf("   📅 %s%n", autor.getPeriodoVida());
                System.out.printf("   📚 Libros registrados: %d | 🆔 ID: %d%n",
                        autor.getLibros().size(), autor.getId());
                System.out.println("   " + "─".repeat(70));
            }

            System.out.printf("\n📊 Total: %d autores registrados%n", autores.size());

        } catch (Exception e) {
            System.out.println("❌ Error al consultar base de datos: " + e.getMessage());
        }
    }

    /**
     * FUNCIONALIDAD PRINCIPAL: Listar autores vivos en un año determinado con
     * validaciones
     */
    private void listarAutoresVivosEnAno() {
        System.out.println("\n📅 === AUTORES VIVOS EN UN AÑO DETERMINADO ===");
        System.out.println("💡 Ingrese un año para ver qué autores estaban vivos en ese momento");
        System.out.println("📅 " + autoresVivosService.obtenerRangoAnosDisponibles());

        boolean entradaValida = false;
        int intentos = 0;
        int maxIntentos = 3;

        while (!entradaValida && intentos < maxIntentos) {
            try {
                System.out.print("\n📆 Ingrese el año (ej: 1850, 1900, 1950): ");
                String entrada = teclado.nextLine().trim();

                // Validar entrada vacía
                if (entrada.isEmpty()) {
                    System.out.println("❌ Error: No puede dejar el año vacío.");
                    intentos++;
                    continue;
                }

                // Validar y parsear año
                Integer ano = autoresVivosService.validarYParsearAno(entrada);

                // Mostrar autores vivos
                autoresVivosService.mostrarAutoresVivosDetallado(ano);

                // Preguntar si quiere hacer otra consulta
                if (preguntarSiContinuar()) {
                    intentos = 0; // Reiniciar contador
                    continue;
                }

                entradaValida = true;

            } catch (IllegalArgumentException e) {
                System.out.println("❌ " + e.getMessage());
                intentos++;

                if (intentos < maxIntentos) {
                    System.out.printf("⚠️ Intento %d de %d. Intente nuevamente.%n", intentos, maxIntentos);
                    mostrarEjemplosEntrada();
                }

            } catch (Exception e) {
                System.out.println("❌ Error inesperado: " + e.getMessage());
                intentos++;
            }
        }

        if (intentos >= maxIntentos) {
            System.out.println("❌ Demasiados intentos fallidos. Regresando al menú principal.");
            mostrarEjemplosEntrada();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n🌍 === LIBROS POR IDIOMA ===");

        List<String> idiomasDisponibles = estadisticasIdiomaService.obtenerIdiomasDisponibles();

        if (idiomasDisponibles.isEmpty()) {
            System.out.println("📭 No hay libros registrados.");
            return;
        }

        System.out.println("🌍 Idiomas disponibles:");
        for (String idioma : idiomasDisponibles) {
            System.out.println("   📖 " + idioma + " - " + obtenerNombreIdioma(idioma));
        }

        System.out.print("\n🔤 Código de idioma: ");

        try {
            String idioma = teclado.nextLine().trim().toLowerCase();

            if (idioma.isEmpty()) {
                System.out.println("❌ Debe ingresar un código de idioma.");
                return;
            }

            estadisticasIdiomaService.listarLibrosPorIdiomaConStreams(idioma);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // ===== FUNCIONALIDADES DE ESTADÍSTICAS POR IDIOMA =====

    /**
     * FUNCIONALIDAD OBLIGATORIA: Exhibir cantidad de libros en un determinado
     * idioma
     */
    private void mostrarEstadisticasPorIdioma() {
        System.out.println("\n📊 === ESTADÍSTICAS POR IDIOMA ESPECÍFICO ===");

        List<String> idiomasDisponibles = estadisticasIdiomaService.obtenerIdiomasDisponibles();

        if (idiomasDisponibles.isEmpty()) {
            System.out.println("📭 No hay libros registrados para analizar.");
            return;
        }

        System.out.println("🌍 Idiomas disponibles:");
        for (String idioma : idiomasDisponibles) {
            System.out.println("   📖 " + idioma + " - " + obtenerNombreIdioma(idioma));
        }

        System.out.print("\n🔤 Ingrese el código del idioma (ej: es, en, fr): ");

        try {
            String idioma = teclado.nextLine().trim().toLowerCase();

            if (idioma.isEmpty()) {
                System.out.println("❌ Debe ingresar un código de idioma.");
                return;
            }

            if (!idiomasDisponibles.contains(idioma)) {
                System.out.println("❌ Idioma no disponible en la base de datos.");
                return;
            }

            estadisticasIdiomaService.exhibirCantidadLibrosPorIdioma(idioma);

        } catch (Exception e) {
            System.out.println("❌ Error al mostrar estadísticas: " + e.getMessage());
        }
    }

    private void mostrarEstadisticasCompletasIdiomas() {
        System.out.println("\n🌍 === ESTADÍSTICAS COMPLETAS DE IDIOMAS ===");

        try {
            estadisticasIdiomaService.mostrarEstadisticasCompletasIdiomas();
        } catch (Exception e) {
            System.out.println("❌ Error al generar estadísticas: " + e.getMessage());
        }
    }

    private void compararDosIdiomas() {
        System.out.println("\n⚖️ === COMPARAR DOS IDIOMAS ===");

        List<String> idiomasDisponibles = estadisticasIdiomaService.obtenerIdiomasDisponibles();

        if (idiomasDisponibles.size() < 2) {
            System.out.println("❌ Se necesitan al menos 2 idiomas para comparar.");
            return;
        }

        System.out.println("🌍 Idiomas disponibles:");
        for (String idioma : idiomasDisponibles) {
            System.out.println("   📖 " + idioma + " - " + obtenerNombreIdioma(idioma));
        }

        try {
            System.out.print("\n🔤 Primer idioma: ");
            String idioma1 = teclado.nextLine().trim().toLowerCase();

            System.out.print("🔤 Segundo idioma: ");
            String idioma2 = teclado.nextLine().trim().toLowerCase();

            if (!idiomasDisponibles.contains(idioma1) || !idiomasDisponibles.contains(idioma2)) {
                System.out.println("❌ Uno o ambos idiomas no están disponibles.");
                return;
            }

            if (idioma1.equals(idioma2)) {
                System.out.println("❌ Debe seleccionar dos idiomas diferentes.");
                return;
            }

            estadisticasIdiomaService.compararIdiomas(idioma1, idioma2);

        } catch (Exception e) {
            System.out.println("❌ Error en la comparación: " + e.getMessage());
        }
    }

    private void mostrarDistribucionIdiomas() {
        System.out.println("\n📊 === DISTRIBUCIÓN POR IDIOMAS ===");

        try {
            estadisticasIdiomaService.mostrarDistribucionIdiomas();
        } catch (Exception e) {
            System.out.println("❌ Error al mostrar distribución: " + e.getMessage());
        }
    }

    // ===== MENÚ AVANZADO DE AUTORES =====

    private void menuAutoresAvanzado() {
        System.out.println("\n👥 === MENÚ AVANZADO DE AUTORES ===");

        var menuAutores = """

                1️⃣ - Autores vivos en un año específico
                2️⃣ - Autores nacidos en un año específico
                3️⃣ - Autores fallecidos en un año específico
                4️⃣ - Autores en un rango de años
                5️⃣ - Estadísticas temporales de autores
                0️⃣ - Volver al menú principal

                Seleccione una opción: """;

        System.out.print(menuAutores);

        try {
            int opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 -> listarAutoresVivosEnAno();
                case 2 -> buscarAutoresPorCriterio("nacidos");
                case 3 -> buscarAutoresPorCriterio("muertos");
                case 4 -> buscarAutoresEnRango();
                case 5 -> mostrarEstadisticasTemporales();
                case 0 -> {
                    /* Volver al menú principal */ }
                default -> System.out.println("❌ Opción no válida.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            teclado.nextLine();
        }
    }

    private void buscarAutoresPorCriterio(String criterio) {
        System.out.printf("\n📅 === AUTORES %s ===", criterio.toUpperCase());
        System.out.print("\n📆 Ingrese el año: ");

        try {
            String entrada = teclado.nextLine().trim();
            Integer ano = autoresVivosService.validarYParsearAno(entrada);
            autoresVivosService.buscarAutoresPorCriterioTemporal(criterio, ano);

        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void buscarAutoresEnRango() {
        System.out.println("\n📅 === AUTORES EN RANGO DE AÑOS ===");

        try {
            System.out.print("📆 Año de inicio: ");
            String entrada1 = teclado.nextLine().trim();
            Integer anoInicio = autoresVivosService.validarYParsearAno(entrada1);

            System.out.print("📆 Año final: ");
            String entrada2 = teclado.nextLine().trim();
            Integer anoFin = autoresVivosService.validarYParsearAno(entrada2);

            autoresVivosService.buscarAutoresEnRango(anoInicio, anoFin);

        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void mostrarEstadisticasTemporales() {
        System.out.println("\n📊 === ESTADÍSTICAS TEMPORALES DE AUTORES ===");
        System.out.println("⚠️ Funcionalidad en desarrollo.");
    }

    // ===== MÉTODOS DE UTILIDAD =====

    /**
     * Preguntar si el usuario quiere continuar con otra consulta
     */
    private boolean preguntarSiContinuar() {
        System.out.print("\n🔄 ¿Desea consultar otro año? (s/n): ");
        String respuesta = teclado.nextLine().trim().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí");
    }

    /**
     * Mostrar ejemplos de entrada válida
     */
    private void mostrarEjemplosEntrada() {
        System.out.println("\n📝 EJEMPLOS DE ENTRADA VÁLIDA:");
        System.out.println("• 1564 (época de Shakespeare)");
        System.out.println("• 1775 (época de Jane Austen)");
        System.out.println("• 1850 (época de Charles Dickens)");
        System.out.println("• 1920 (época de Virginia Woolf)");
        System.out.println("• Solo números enteros positivos");
        System.out.println("• Años desde 1 hasta " + java.time.Year.now().getValue());
    }

    private void mostrarBienvenida() {
        System.out.println("""

                ╔═══════════════════════════════════════════════════════════╗
                ║                                                           ║
                ║          📚 LITERALURA - POSTGRESQL 📚                   ║
                ║                                                           ║
                ║     Catálogo completo con estadísticas avanzadas         ║
                ║                                                           ║
                ║  🔍 Busca y guarda libros de Gutendx                     ║
                ║  📊 Estadísticas detalladas por idioma                   ║
                ║  👥 Consultas avanzadas de autores                       ║
                ║  🗄️ Persistencia en PostgreSQL                           ║
                ║                                                           ║
                ╚═══════════════════════════════════════════════════════════╝
                """);
    }

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

    private void esperarEnter() {
        System.out.println("\n⏎ Presione ENTER para continuar...");
        teclado.nextLine();
    }
}
