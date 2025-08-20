package com.alura.literalura.repository;

import com.alura.literalura.entity.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
    
    // Buscar libro por título exacto
    Optional<LibroEntity> findByTitulo(String titulo);
    
    // Buscar libros por título que contenga texto (case insensitive)
    List<LibroEntity> findByTituloContainingIgnoreCase(String titulo);
    
    // ===== DERIVED QUERIES PARA IDIOMAS =====
    
    // Buscar libros por idioma específico
    List<LibroEntity> findByIdioma(String idioma);
    
    // Buscar libros por idioma ordenados por número de descargas (descendente)
    List<LibroEntity> findByIdiomaOrderByNumeroDescargasDesc(String idioma);
    
    // Buscar libros por idioma ordenados por título
    List<LibroEntity> findByIdiomaOrderByTituloAsc(String idioma);
    
    // Contar libros por idioma específico
    Long countByIdioma(String idioma);
    
    // Buscar libros en inglés
    @Query("SELECT l FROM LibroEntity l WHERE l.idioma = 'en' ORDER BY l.numeroDescargas DESC")
    List<LibroEntity> findLibrosEnInglesOrdenadosPorDescargas();
    
    // Buscar libros en español
    @Query("SELECT l FROM LibroEntity l WHERE l.idioma = 'es' ORDER BY l.numeroDescargas DESC")
    List<LibroEntity> findLibrosEnEspanolOrdenadosPorDescargas();
    
    // Buscar libros en francés
    @Query("SELECT l FROM LibroEntity l WHERE l.idioma = 'fr' ORDER BY l.numeroDescargas DESC")
    List<LibroEntity> findLibrosEnFrancesOrdenadosPorDescargas();
    
    // Buscar libros en portugués
    @Query("SELECT l FROM LibroEntity l WHERE l.idioma = 'pt' ORDER BY l.numeroDescargas DESC")
    List<LibroEntity> findLibrosEnPortuguesOrdenadosPorDescargas();
    
    // ===== CONSULTAS PARA ESTADÍSTICAS =====
    
    // Contar libros por idioma (estadísticas completas)
    @Query("SELECT l.idioma, COUNT(l) FROM LibroEntity l GROUP BY l.idioma ORDER BY COUNT(l) DESC")
    List<Object[]> countLibrosPorIdioma();
    
    // Obtener idiomas únicos disponibles
    @Query("SELECT DISTINCT l.idioma FROM LibroEntity l ORDER BY l.idioma")
    List<String> findIdiomasDisponibles();
    
    // Estadísticas de descargas por idioma
    @Query("SELECT l.idioma, COUNT(l), SUM(l.numeroDescargas), AVG(l.numeroDescargas), MAX(l.numeroDescargas) " +
           "FROM LibroEntity l WHERE l.numeroDescargas IS NOT NULL " +
           "GROUP BY l.idioma ORDER BY COUNT(l) DESC")
    List<Object[]> getEstadisticasDescargasPorIdioma();
    
    // ===== MÉTODOS FALTANTES =====
    
    // Obtener estadísticas generales de descargas
    @Query("SELECT MIN(l.numeroDescargas), MAX(l.numeroDescargas), AVG(l.numeroDescargas) " +
           "FROM LibroEntity l WHERE l.numeroDescargas IS NOT NULL")
    List<Object[]> getEstadisticasDescargas();
    
    // Buscar libros por ID del autor
    List<LibroEntity> findByAutorId(Long autorId);
    
    // ===== OTROS MÉTODOS =====
    
    // Buscar libros por autor
    List<LibroEntity> findByAutorNombreContainingIgnoreCase(String nombreAutor);
    
    // Buscar libros más descargados
    List<LibroEntity> findTop10ByOrderByNumeroDescargasDesc();
    
    // Verificar si existe libro por ID
    boolean existsById(Long id);
    
    // Buscar todos los libros ordenados por título
    List<LibroEntity> findAllByOrderByTituloAsc();
    
    // Buscar libros con más de X descargas
    List<LibroEntity> findByNumeroDescargasGreaterThanOrderByNumeroDescargasDesc(Integer numeroDescargas);
    
    // Buscar libros por autor y idioma
    List<LibroEntity> findByAutorNombreContainingIgnoreCaseAndIdioma(String nombreAutor, String idioma);
}
