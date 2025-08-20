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
public class PersistenciaService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GutendxServiceMejorado gutendxService;

    @Autowired
    private LibroConverter libroConverter;

    /**
     * Buscar libro en API y persistir en base de datos
     */
    public LibroEntity buscarYPersistirLibro(String titulo) {
        try {
            System.out.println("🔍 Iniciando búsqueda y persistencia para: " + titulo);

            // 1. Buscar en la API
            RespuestaLibros respuesta = gutendxService.buscarLibros(titulo);

            if (respuesta.getLibros() == null || respuesta.getLibros().isEmpty()) {
                System.out.println("❌ No se encontraron libros en la API");
                return null;
            }

            // 2. Tomar el primer resultado
            Libro libroDto = respuesta.getLibros().get(0);
            System.out.println("📖 Libro encontrado en API: " + libroDto.getTitulo());

            // 3. Verificar si ya existe en BD
            if (libroRepository.existsById(libroDto.getId())) {
                System.out.println("⚠️ Libro ya existe en la base de datos");
                return libroRepository.findById(libroDto.getId()).orElse(null);
            }

            // 4. Convertir DTO a Entity
            LibroEntity libroEntity = libroConverter.crearEntityConValidacion(libroDto);

            // 5. Persistir autor (buscar existente o crear nuevo)
            if (libroEntity.getAutor() != null) {
                AutorEntity autorPersistido = persistirAutor(libroEntity.getAutor());
                libroEntity.setAutor(autorPersistido);
            }

            // 6. Persistir libro
            LibroEntity libroGuardado = libroRepository.save(libroEntity);
            System.out.println("✅ Libro persistido exitosamente con ID: " + libroGuardado.getId());

            // 7. Actualizar relación bidireccional
            if (libroGuardado.getAutor() != null) {
                libroGuardado.getAutor().agregarLibro(libroGuardado);
                autorRepository.save(libroGuardado.getAutor());
            }

            return libroGuardado;

        } catch (Exception e) {
            System.err.println("❌ Error en persistencia: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Persistir autor (buscar existente o crear nuevo)
     */
    private AutorEntity persistirAutor(AutorEntity autor) {
        if (autor == null || autor.getNombre() == null) {
            System.out.println("⚠️ Autor nulo o sin nombre, no se puede persistir");
            return null;
        }

        // Buscar autor existente por nombre
        Optional<AutorEntity> autorExistente = autorRepository.findByNombre(autor.getNombre());

        if (autorExistente.isPresent()) {
            System.out.println("👤 Autor ya existe en BD: " + autor.getNombre());
            return autorExistente.get();
        } else {
            // Crear nuevo autor
            AutorEntity nuevoAutor = autorRepository.save(autor);
            System.out
                    .println("👤 Nuevo autor persistido: " + nuevoAutor.getNombre() + " con ID: " + nuevoAutor.getId());
            return nuevoAutor;
        }
    }

    /**
     * Listar todos los libros con información de persistencia
     */
    public List<LibroEntity> listarTodosLosLibrosPersistidos() {
        System.out.println("📚 Obteniendo libros de la base de datos...");

        List<LibroEntity> libros = libroRepository.findAllByOrderByTituloAsc();

        System.out.println("📊 Libros encontrados en BD: " + libros.size());

        return libros;
    }

    /**
     * Listar todos los autores con información de persistencia
     */
    public List<AutorEntity> listarTodosLosAutoresPersistidos() {
        System.out.println("👥 Obteniendo autores de la base de datos...");

        List<AutorEntity> autores = autorRepository.findAllByOrderByNombreAsc();

        System.out.println("📊 Autores encontrados en BD: " + autores.size());

        return autores;
    }

    /**
     * Listar autores vivos en un año específico
     */
    public List<AutorEntity> listarAutoresVivosEnAno(Integer ano) {
        System.out.println("📅 Buscando autores vivos en " + ano + " en la base de datos...");

        List<AutorEntity> autores = autorRepository.findAutoresVivosEnAno(ano);

        System.out.println("📊 Autores vivos en " + ano + ": " + autores.size());

        return autores;
    }

    /**
     * Listar libros por idioma desde la base de datos
     */
    public List<LibroEntity> listarLibrosPorIdioma(String idioma) {
        System.out.println("🌍 Buscando libros en idioma '" + idioma + "' en la base de datos...");

        List<LibroEntity> libros = libroRepository.findByIdiomaOrderByNumeroDescargasDesc(idioma.toLowerCase());

        System.out.println("📊 Libros en " + idioma + ": " + libros.size());

        return libros;
    }

    /**
     * Obtener estadísticas completas de la base de datos
     */
    public void mostrarEstadisticasCompletas() {
        System.out.println("📊 === ESTADÍSTICAS COMPLETAS DE LA BASE DE DATOS ===");

        // Estadísticas básicas
        long totalLibros = libroRepository.count();
        long totalAutores = autorRepository.count();

        System.out.println("📚 Total de libros: " + totalLibros);
        System.out.println("👥 Total de autores: " + totalAutores);

        // Estadísticas de idiomas
        List<Object[]> estadisticasIdioma = libroRepository.countLibrosPorIdioma();
        System.out.println("\n🌍 Distribución por idiomas:");
        estadisticasIdioma.forEach(stat -> System.out.println("   " + stat[0] + ": " + stat[1] + " libros"));

        // Estadísticas de descargas
        List<Object[]> estadisticasDescargas = libroRepository.getEstadisticasDescargas();
        if (!estadisticasDescargas.isEmpty()) {
            Object[] stats = estadisticasDescargas.get(0);
            System.out.println("\n📥 Estadísticas de descargas:");
            System.out.println("   Mínimo: " + stats[0]);
            System.out.println("   Máximo: " + stats[1]);
            System.out.println("   Promedio: " + String.format("%.2f", stats[2]));
        }

        // Estadísticas de autores por siglo
        List<Object[]> autoresPorSiglo = autorRepository.countAutoresPorSiglo();
        System.out.println("\n📅 Autores por siglo:");
        autoresPorSiglo.forEach(stat -> System.out.println("   Siglo " + stat[0] + ": " + stat[1] + " autores"));

        System.out.println("═".repeat(50));
    }

    /**
     * Validar integridad de la base de datos
     */
    public void validarIntegridadBaseDatos() {
        System.out.println("🔍 === VALIDACIÓN DE INTEGRIDAD ===");

        // Verificar libros sin autor
        List<LibroEntity> libros = libroRepository.findAll();
        long librosSinAutor = libros.stream()
                .filter(libro -> libro.getAutor() == null)
                .count();

        if (librosSinAutor > 0) {
            System.out.println("⚠️ Libros sin autor: " + librosSinAutor);
        } else {
            System.out.println("✅ Todos los libros tienen autor asignado");
        }

        // Verificar autores sin libros
        List<AutorEntity> autoresSinLibros = autorRepository.findAll().stream()
                .filter(autor -> autor.getLibros() == null || autor.getLibros().isEmpty())
                .toList();

        if (!autoresSinLibros.isEmpty()) {
            System.out.println("⚠️ Autores sin libros: " + autoresSinLibros.size());
        } else {
            System.out.println("✅ Todos los autores tienen al menos un libro");
        }

        System.out.println("═".repeat(50));
    }

    /**
     * Eliminar libro y manejar relación con autor
     */
    @Transactional
    public boolean eliminarLibro(Long libroId) {
        try {
            Optional<LibroEntity> libroOpt = libroRepository.findById(libroId);

            if (libroOpt.isEmpty()) {
                System.out.println("❌ Libro no encontrado con ID: " + libroId);
                return false;
            }

            LibroEntity libro = libroOpt.get();
            AutorEntity autor = libro.getAutor();

            // Eliminar libro
            libroRepository.delete(libro);
            System.out.println("✅ Libro eliminado: " + libro.getTitulo());

            // Verificar si el autor se queda sin libros
            if (autor != null) {
                List<LibroEntity> librosDelAutor = libroRepository.findByAutorId(autor.getId());
                if (librosDelAutor.isEmpty()) {
                    autorRepository.delete(autor);
                    System.out.println("✅ Autor eliminado (sin libros): " + autor.getNombre());
                }
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
}
