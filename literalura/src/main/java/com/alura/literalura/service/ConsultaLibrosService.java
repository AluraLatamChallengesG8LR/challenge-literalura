package com.alura.literalura.service;

import com.alura.literalura.converter.LibroConverter;
import com.alura.literalura.entity.AutorEntity;
import com.alura.literalura.entity.LibroEntity;
import com.alura.literalura.model.Libro;
import com.alura.literalura.model.RespuestaLibros;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConsultaLibrosService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GutendxServiceMejorado gutendxService;

    @Autowired
    private LibroConverter libroConverter;

    /**
     * FUNCIONALIDAD OBLIGATORIA 1: Búsqueda de libro por título
     */
    public LibroEntity buscarLibroPorTitulo(String titulo) {
        try {
            System.out.println("🔍 Buscando libro por título: " + titulo);

            // Buscar en la API de Gutendx
            RespuestaLibros respuesta = gutendxService.buscarLibros(titulo);

            if (respuesta.getLibros() == null || respuesta.getLibros().isEmpty()) {
                System.out.println("❌ No se encontraron libros con ese título en la API");
                return null;
            }

            // Tomar el primer resultado
            Libro libroDto = respuesta.getLibros().get(0);
            System.out.println("📖 Primer resultado encontrado: " + libroDto.getTitulo());

            // Verificar si ya existe en la base de datos
            if (libroRepository.existsById(libroDto.getId())) {
                System.out.println("⚠️ El libro ya está registrado en la base de datos");
                return libroRepository.findById(libroDto.getId()).orElse(null);
            }

            // Convertir y guardar el libro
            LibroEntity libroEntity = libroConverter.crearEntityConValidacion(libroDto);

            // Guardar o buscar autor existente
            if (libroEntity.getAutor() != null) {
                AutorEntity autorGuardado = guardarOBuscarAutor(libroEntity.getAutor());
                libroEntity.setAutor(autorGuardado);
            }

            // Guardar libro
            LibroEntity libroGuardado = libroRepository.save(libroEntity);
            System.out.println("✅ Libro guardado exitosamente en la base de datos");

            return libroGuardado;

        } catch (Exception e) {
            System.err.println("❌ Error al buscar libro por título: " + e.getMessage());
            return null;
        }
    }

    /**
     * FUNCIONALIDAD OBLIGATORIA 2: Lista de todos los libros
     */
    public List<LibroEntity> listarTodosLosLibros() {
        System.out.println("📚 Obteniendo lista de todos los libros registrados...");

        List<LibroEntity> libros = libroRepository.findAllByOrderByTituloAsc();

        System.out.println("📊 Total de libros encontrados: " + libros.size());

        return libros;
    }

    /**
     * FUNCIONALIDAD ADICIONAL: Lista de libros por idioma (derived query)
     */
    public List<LibroEntity> listarLibrosPorIdioma(String idioma) {
        System.out.println("🌍 Buscando libros en idioma: " + idioma);

        List<LibroEntity> libros = libroRepository.findByIdiomaOrderByNumeroDescargasDesc(idioma.toLowerCase());

        System.out.println("📊 Libros encontrados en " + idioma + ": " + libros.size());

        return libros;
    }

    /**
     * Obtener estadísticas de idiomas
     */
    public void mostrarEstadisticasIdiomas() {
        System.out.println("📊 === ESTADÍSTICAS POR IDIOMA ===");

        List<Object[]> estadisticas = libroRepository.countLibrosPorIdioma();

        if (estadisticas.isEmpty()) {
            System.out.println("📭 No hay libros registrados para mostrar estadísticas");
            return;
        }

        System.out.println("🌍 Distribución de libros por idioma:");
        System.out.println("═".repeat(40));

        for (Object[] stat : estadisticas) {
            String idioma = (String) stat[0];
            Long cantidad = (Long) stat[1];
            String nombreIdioma = obtenerNombreIdioma(idioma);

            System.out.printf("📖 %-15s: %d libros%n", nombreIdioma, cantidad);
        }

        System.out.println("═".repeat(40));
    }

    /**
     * Validar entrada de usuario para idiomas
     */
    public boolean esIdiomaValido(String idioma) {
        if (idioma == null || idioma.trim().isEmpty()) {
            return false;
        }

        String idiomaLower = idioma.toLowerCase().trim();
        return idiomaLower.matches("^[a-z]{2}$"); // Solo códigos de 2 letras
    }

    /**
     * Obtener idiomas disponibles en la base de datos
     */
    public List<String> obtenerIdiomasDisponibles() {
        return libroRepository.countLibrosPorIdioma()
                .stream()
                .map(stat -> (String) stat[0])
                .toList();
    }

    /**
     * Buscar libros populares (más de 1000 descargas)
     */
    public List<LibroEntity> obtenerLibrosPopulares() {
        return libroRepository.findByNumeroDescargasGreaterThanOrderByNumeroDescargasDesc(1000);
    }

    /**
     * Guardar o buscar autor existente
     */
    private AutorEntity guardarOBuscarAutor(AutorEntity autor) {
        if (autor == null || autor.getNombre() == null) {
            return null;
        }

        Optional<AutorEntity> autorExistente = autorRepository.findByNombre(autor.getNombre());

        if (autorExistente.isPresent()) {
            System.out.println("👤 Autor ya existe en la base de datos: " + autor.getNombre());
            return autorExistente.get();
        } else {
            AutorEntity autorGuardado = autorRepository.save(autor);
            System.out.println("👤 Nuevo autor guardado: " + autorGuardado.getNombre());
            return autorGuardado;
        }
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
