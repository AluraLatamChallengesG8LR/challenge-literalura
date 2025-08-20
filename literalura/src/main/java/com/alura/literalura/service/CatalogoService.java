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
public class CatalogoService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private GutendxServiceMejorado gutendxService;

    @Autowired
    private ProcesadorDatos procesador;

    @Autowired
    private LibroConverter libroConverter;

    /**
     * Buscar y guardar libro por título
     */
    public LibroEntity buscarYGuardarLibro(String titulo) {
        try {
            System.out.println("🔍 Buscando libro: " + titulo);

            RespuestaLibros respuesta = gutendxService.buscarLibros(titulo);

            if (respuesta.getLibros() == null || respuesta.getLibros().isEmpty()) {
                System.out.println("❌ No se encontraron libros con ese título");
                return null;
            }

            // Tomar el primer libro encontrado
            Libro libroDto = respuesta.getLibros().get(0);

            // Verificar si ya existe en la base de datos
            if (libroRepository.existsById(libroDto.getId())) {
                System.out.println("⚠️ El libro ya está registrado en la base de datos");
                return libroRepository.findById(libroDto.getId()).orElse(null);
            }

            // Procesar y guardar el libro
            LibroEntity libroEntity = procesador.procesarLibroIndividual(libroDto);

            if (libroEntity != null) {
                // Guardar o buscar autor existente
                AutorEntity autorGuardado = guardarOBuscarAutor(libroEntity.getAutor());
                libroEntity.setAutor(autorGuardado);

                // Guardar libro
                LibroEntity libroGuardado = libroRepository.save(libroEntity);
                System.out.println("✅ Libro guardado exitosamente: " + libroGuardado.getTitulo());
                return libroGuardado;
            }

            return null;

        } catch (Exception e) {
            System.err.println("❌ Error al buscar y guardar libro: " + e.getMessage());
            return null;
        }
    }

    /**
     * Listar todos los libros registrados
     */
    public List<LibroEntity> listarTodosLosLibros() {
        List<LibroEntity> libros = libroRepository.findAll();
        System.out.println("📚 Total de libros registrados: " + libros.size());
        return libros;
    }

    /**
     * Listar todos los autores registrados
     */
    public List<AutorEntity> listarTodosLosAutores() {
        List<AutorEntity> autores = autorRepository.findAll();
        System.out.println("👥 Total de autores registrados: " + autores.size());
        return autores;
    }

    /**
     * Listar autores vivos en un año específico
     */
    public List<AutorEntity> listarAutoresVivosEnAno(Integer ano) {
        List<AutorEntity> autores = autorRepository.findAutoresVivosEnAno(ano);
        System.out.println("👤 Autores vivos en " + ano + ": " + autores.size());
        return autores;
    }

    /**
     * Listar libros por idioma
     */
    public List<LibroEntity> listarLibrosPorIdioma(String idioma) {
        List<LibroEntity> libros = libroRepository.findByIdioma(idioma.toLowerCase());
        System.out.println("🌍 Libros en " + idioma + ": " + libros.size());
        return libros;
    }

    /**
     * Buscar libros por título en la base de datos
     */
    public List<LibroEntity> buscarLibrosPorTitulo(String titulo) {
        List<LibroEntity> libros = libroRepository.findByTituloContainingIgnoreCase(titulo);
        System.out.println("🔍 Libros encontrados con '" + titulo + "': " + libros.size());
        return libros;
    }

    /**
     * Buscar libros por autor en la base de datos
     */
    public List<LibroEntity> buscarLibrosPorAutor(String nombreAutor) {
        List<LibroEntity> libros = libroRepository.findByAutorNombreContainingIgnoreCase(nombreAutor);
        System.out.println("👤 Libros del autor '" + nombreAutor + "': " + libros.size());
        return libros;
    }

    /**
     * Obtener top 10 libros más descargados
     */
    public List<LibroEntity> obtenerTop10LibrosMasDescargados() {
        List<LibroEntity> libros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();
        System.out.println("🏆 Top 10 libros más descargados obtenido");
        return libros;
    }

    /**
     * Obtener estadísticas del catálogo
     */
    public void mostrarEstadisticasCatalogo() {
        long totalLibros = libroRepository.count();
        long totalAutores = autorRepository.count();

        System.out.println("📊 === ESTADÍSTICAS DEL CATÁLOGO ===");
        System.out.println("📚 Total de libros: " + totalLibros);
        System.out.println("👥 Total de autores: " + totalAutores);

        // Estadísticas por idioma
        List<Object[]> estadisticasIdioma = libroRepository.countLibrosPorIdioma();
        System.out.println("🌍 Libros por idioma:");
        estadisticasIdioma.forEach(stat -> System.out.println("   " + stat[0] + ": " + stat[1] + " libros"));

        System.out.println("=====================================");
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
            return autorExistente.get();
        } else {
            return autorRepository.save(autor);
        }
    }
}
