package com.alura.literalura.service;

import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutoresVivosService {

    @Autowired
    private AutorRepository autorRepository;

    /**
     * FUNCIONALIDAD PRINCIPAL: Listar autores vivos en determinado año
     */
    public List<AutorEntity> listarAutoresVivosEnAno(Integer ano) {
        // Validar año
        if (!esAnoValido(ano)) {
            throw new IllegalArgumentException("Año inválido: " + ano);
        }

        System.out.println("📅 Buscando autores vivos en el año " + ano + "...");

        // Usar derived query
        List<AutorEntity> autoresVivos = autorRepository.findAutoresVivosEnAno(ano);

        System.out.println("👥 Autores encontrados: " + autoresVivos.size());

        return autoresVivos;
    }

    /**
     * Mostrar autores vivos con detalles completos
     */
    public void mostrarAutoresVivosDetallado(Integer ano) {
        try {
            List<AutorEntity> autores = listarAutoresVivosEnAno(ano);

            if (autores.isEmpty()) {
                System.out.println("📭 No se encontraron autores vivos en el año " + ano);
                mostrarSugerencias(ano);
                return;
            }

            System.out.println("═".repeat(80));
            System.out.println("👥 AUTORES VIVOS EN " + ano);
            System.out.println("═".repeat(80));

            for (int i = 0; i < autores.size(); i++) {
                AutorEntity autor = autores.get(i);
                System.out.printf("%d. 👤 %s%n", (i + 1), autor.getNombre());
                System.out.printf("   📅 Nacimiento: %s%n",
                        autor.getAnoNacimiento() != null ? autor.getAnoNacimiento() : "Desconocido");
                System.out.printf("   ⚰️ Fallecimiento: %s%n",
                        autor.getAnoFallecimiento() != null ? autor.getAnoFallecimiento() : "Vivo");

                // Calcular edad en el año consultado
                if (autor.getAnoNacimiento() != null) {
                    int edadEnAno = ano - autor.getAnoNacimiento();
                    System.out.printf("   🎂 Edad en %d: %d años%n", ano, edadEnAno);
                }

                // Mostrar libros si los tiene
                if (autor.getLibros() != null && !autor.getLibros().isEmpty()) {
                    System.out.printf("   📚 Libros registrados: %d%n", autor.getLibros().size());
                    if (autor.getLibros().size() <= 3) {
                        autor.getLibros().forEach(libro -> System.out.printf("      📖 %s%n", libro.getTitulo()));
                    } else {
                        System.out.printf("      📖 %s (y %d más)%n",
                                autor.getLibros().get(0).getTitulo(),
                                autor.getLibros().size() - 1);
                    }
                }

                System.out.println("   " + "─".repeat(70));
            }

            // Mostrar estadísticas del grupo
            mostrarEstadisticasGrupo(autores, ano);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
            mostrarEjemplosAnosValidos();
        } catch (Exception e) {
            System.out.println("❌ Error al buscar autores: " + e.getMessage());
        }
    }

    /**
     * Validar si un año es válido
     */
    public boolean esAnoValido(Integer ano) {
        if (ano == null) {
            return false;
        }

        int anoActual = Year.now().getValue();

        // Validar rango razonable (desde año 1 hasta año actual)
        return ano >= 1 && ano <= anoActual;
    }

    /**
     * Obtener rango de años disponibles en la base de datos
     */
    public String obtenerRangoAnosDisponibles() {
        try {
            List<Object[]> estadisticas = autorRepository.getEstadisticasAnosNacimiento();

            if (estadisticas.isEmpty() || estadisticas.get(0)[0] == null) {
                return "No hay datos de años disponibles";
            }

            Object[] stats = estadisticas.get(0);
            Integer anoMinimo = ((Number) stats[0]).intValue();
            Integer anoMaximo = ((Number) stats[1]).intValue();

            return String.format("Años disponibles: %d - %d", anoMinimo, anoMaximo);

        } catch (Exception e) {
            return "Error al obtener rango de años";
        }
    }

    /**
     * Buscar autores por diferentes criterios temporales
     */
    public void buscarAutoresPorCriterioTemporal(String criterio, Integer ano) {
        try {
            List<AutorEntity> autores;
            String titulo;

            switch (criterio.toLowerCase()) {
                case "nacidos" -> {
                    autores = autorRepository.findByAnoNacimiento(ano);
                    titulo = "AUTORES NACIDOS EN " + ano;
                }
                case "muertos" -> {
                    autores = autorRepository.findByAnoFallecimiento(ano);
                    titulo = "AUTORES FALLECIDOS EN " + ano;
                }
                case "antes" -> {
                    autores = autorRepository.findByAnoNacimientoLessThanEqual(ano);
                    titulo = "AUTORES NACIDOS ANTES O EN " + ano;
                }
                case "despues" -> {
                    autores = autorRepository.findByAnoNacimientoGreaterThanEqual(ano);
                    titulo = "AUTORES NACIDOS DESPUÉS O EN " + ano;
                }
                default -> {
                    System.out.println("❌ Criterio no válido: " + criterio);
                    return;
                }
            }

            System.out.println("═".repeat(60));
            System.out.println("👥 " + titulo);
            System.out.println("═".repeat(60));

            if (autores.isEmpty()) {
                System.out.println("📭 No se encontraron autores con ese criterio");
                return;
            }

            autores.forEach(autor -> {
                System.out.printf("👤 %s (%s)%n",
                        autor.getNombre(),
                        autor.getPeriodoVida());
            });

            System.out.printf("📊 Total: %d autores%n", autores.size());

        } catch (Exception e) {
            System.out.println("❌ Error en búsqueda: " + e.getMessage());
        }
    }

    /**
     * Mostrar estadísticas del grupo de autores
     */
    private void mostrarEstadisticasGrupo(List<AutorEntity> autores, Integer ano) {
        System.out.println("\n📊 ESTADÍSTICAS DEL GRUPO:");

        // Edad promedio en el año consultado
        double edadPromedio = autores.stream()
                .filter(autor -> autor.getAnoNacimiento() != null)
                .mapToInt(autor -> ano - autor.getAnoNacimiento())
                .average()
                .orElse(0.0);

        // Autor más joven y más viejo en ese año
        var autorMasJoven = autores.stream()
                .filter(autor -> autor.getAnoNacimiento() != null)
                .min((a1, a2) -> Integer.compare(a2.getAnoNacimiento(), a1.getAnoNacimiento()));

        var autorMasViejo = autores.stream()
                .filter(autor -> autor.getAnoNacimiento() != null)
                .max((a1, a2) -> Integer.compare(a2.getAnoNacimiento(), a1.getAnoNacimiento()));

        // Total de libros del grupo
        int totalLibros = autores.stream()
                .mapToInt(autor -> autor.getLibros() != null ? autor.getLibros().size() : 0)
                .sum();

        System.out.printf("🎂 Edad promedio en %d: %.1f años%n", ano, edadPromedio);
        System.out.printf("📚 Total de libros del grupo: %d%n", totalLibros);

        if (autorMasJoven.isPresent()) {
            AutorEntity joven = autorMasJoven.get();
            int edadJoven = ano - joven.getAnoNacimiento();
            System.out.printf("👶 Más joven: %s (%d años)%n", joven.getNombre(), edadJoven);
        }

        if (autorMasViejo.isPresent()) {
            AutorEntity viejo = autorMasViejo.get();
            int edadViejo = ano - viejo.getAnoNacimiento();
            System.out.printf("👴 Más viejo: %s (%d años)%n", viejo.getNombre(), edadViejo);
        }
    }

    /**
     * Mostrar sugerencias cuando no se encuentran autores
     */
    private void mostrarSugerencias(Integer ano) {
        System.out.println("\n💡 SUGERENCIAS:");
        System.out.println("• " + obtenerRangoAnosDisponibles());

        // Sugerir años cercanos con autores
        List<Integer> anosCercanos = List.of(ano - 50, ano - 25, ano + 25, ano + 50);

        for (Integer anoCercano : anosCercanos) {
            if (esAnoValido(anoCercano)) {
                List<AutorEntity> autoresCercanos = autorRepository.findAutoresVivosEnAno(anoCercano);
                if (!autoresCercanos.isEmpty()) {
                    System.out.printf("• Pruebe con el año %d (%d autores disponibles)%n",
                            anoCercano, autoresCercanos.size());
                    break;
                }
            }
        }
    }

    /**
     * Mostrar ejemplos de años válidos
     */
    private void mostrarEjemplosAnosValidos() {
        int anoActual = Year.now().getValue();
        System.out.println("\n📅 EJEMPLOS DE AÑOS VÁLIDOS:");
        System.out.println("• Siglo XVI: 1500-1600");
        System.out.println("• Siglo XVII: 1600-1700");
        System.out.println("• Siglo XVIII: 1700-1800");
        System.out.println("• Siglo XIX: 1800-1900");
        System.out.println("• Siglo XX: 1900-2000");
        System.out.printf("• Siglo XXI: 2000-%d%n", anoActual);
        System.out.println("• " + obtenerRangoAnosDisponibles());
    }

    /**
     * Validar entrada del usuario con manejo de errores
     */
    public Integer validarYParsearAno(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            throw new IllegalArgumentException("El año no puede estar vacío");
        }

        try {
            Integer ano = Integer.parseInt(entrada.trim());

            if (!esAnoValido(ano)) {
                throw new IllegalArgumentException(
                        String.format("Año fuera de rango válido (1-%d): %d",
                                Year.now().getValue(), ano));
            }

            return ano;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("'%s' no es un año válido. Ingrese un número entero.", entrada));
        }
    }

    /**
     * Buscar autores en un rango de años
     */
    public void buscarAutoresEnRango(Integer anoInicio, Integer anoFin) {
        try {
            if (!esAnoValido(anoInicio) || !esAnoValido(anoFin)) {
                throw new IllegalArgumentException("Uno o ambos años son inválidos");
            }

            if (anoInicio > anoFin) {
                throw new IllegalArgumentException("El año de inicio no puede ser mayor al año final");
            }

            System.out.printf("📅 Buscando autores que vivieron entre %d y %d...%n", anoInicio, anoFin);

            // Usar derived query para rango
            List<AutorEntity> autores = autorRepository.findByAnoNacimientoBetween(anoInicio, anoFin);

            if (autores.isEmpty()) {
                System.out.printf("📭 No se encontraron autores nacidos entre %d y %d%n", anoInicio, anoFin);
                return;
            }

            System.out.printf("👥 Autores nacidos entre %d y %d:%n", anoInicio, anoFin);
            System.out.println("═".repeat(60));

            // Agrupar por siglo para mejor visualización
            var autoresPorSiglo = autores.stream()
                    .filter(autor -> autor.getAnoNacimiento() != null)
                    .collect(Collectors.groupingBy(autor -> (autor.getAnoNacimiento() / 100 + 1) * 100));

            autoresPorSiglo.entrySet().stream()
                    .sorted(java.util.Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        Integer siglo = entry.getKey();
                        List<AutorEntity> autoresDelSiglo = entry.getValue();

                        System.out.printf("📅 Siglo %s:%n", obtenerNombreSiglo(siglo));
                        autoresDelSiglo.forEach(autor -> System.out.printf("   👤 %s (%d)%n",
                                autor.getNombre(), autor.getAnoNacimiento()));
                        System.out.println();
                    });

            System.out.printf("📊 Total: %d autores%n", autores.size());

        } catch (Exception e) {
            System.out.println("❌ Error en búsqueda por rango: " + e.getMessage());
        }
    }

    /**
     * Obtener nombre del siglo
     */
    private String obtenerNombreSiglo(Integer ano) {
        int siglo = ano / 100;
        return switch (siglo) {
            case 15 -> "XV";
            case 16 -> "XVI";
            case 17 -> "XVII";
            case 18 -> "XVIII";
            case 19 -> "XIX";
            case 20 -> "XX";
            case 21 -> "XXI";
            default -> String.valueOf(siglo);
        };
    }
}
