package com.alura.literalura.repository;

import com.alura.literalura.entity.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<AutorEntity, Long> {

    // Buscar autor por nombre exacto
    Optional<AutorEntity> findByNombre(String nombre);

    // Buscar autores por nombre que contenga texto (case insensitive)
    List<AutorEntity> findByNombreContainingIgnoreCase(String nombre);

    // ===== DERIVED QUERIES PARA AUTORES VIVOS =====

    // Autores vivos en un año específico (derived query)
    @Query("SELECT a FROM AutorEntity a WHERE a.anoNacimiento <= :ano AND (a.anoFallecimiento IS NULL OR a.anoFallecimiento >= :ano)")
    List<AutorEntity> findAutoresVivosEnAno(@Param("ano") Integer ano);

    // Autores que nacieron en un año específico
    List<AutorEntity> findByAnoNacimiento(Integer anoNacimiento);

    // Autores que murieron en un año específico
    List<AutorEntity> findByAnoFallecimiento(Integer anoFallecimiento);

    // Autores nacidos antes de un año
    List<AutorEntity> findByAnoNacimientoLessThanEqual(Integer ano);

    // Autores nacidos después de un año
    List<AutorEntity> findByAnoNacimientoGreaterThanEqual(Integer ano);

    // Autores que murieron después de un año
    List<AutorEntity> findByAnoFallecimientoGreaterThanEqual(Integer ano);

    // Autores que murieron antes de un año
    List<AutorEntity> findByAnoFallecimientoLessThanEqual(Integer ano);

    // Autores vivos (sin año de fallecimiento)
    List<AutorEntity> findByAnoFallecimientoIsNull();

    // Autores muertos (con año de fallecimiento)
    List<AutorEntity> findByAnoFallecimientoIsNotNull();

    // Autores por rango de años de nacimiento
    List<AutorEntity> findByAnoNacimientoBetween(Integer anoInicio, Integer anoFin);

    // Autores por rango de años de fallecimiento
    List<AutorEntity> findByAnoFallecimientoBetween(Integer anoInicio, Integer anoFin);

    // ===== CONSULTAS ADICIONALES =====

    // Buscar autores que tienen libros registrados
    @Query("SELECT DISTINCT a FROM AutorEntity a WHERE SIZE(a.libros) > 0")
    List<AutorEntity> findAutoresConLibros();

    // Buscar autores ordenados por nombre
    List<AutorEntity> findAllByOrderByNombreAsc();

    // Contar autores por siglo de nacimiento
    @Query("SELECT FLOOR(a.anoNacimiento/100) + 1 as siglo, COUNT(a) FROM AutorEntity a WHERE a.anoNacimiento IS NOT NULL GROUP BY FLOOR(a.anoNacimiento/100) + 1 ORDER BY siglo")
    List<Object[]> countAutoresPorSiglo();

    // Obtener estadísticas de años de vida
    @Query("SELECT MIN(a.anoNacimiento), MAX(a.anoNacimiento), AVG(a.anoNacimiento) FROM AutorEntity a WHERE a.anoNacimiento IS NOT NULL")
    List<Object[]> getEstadisticasAnosNacimiento();

    // Autores más longevos (con años de nacimiento y fallecimiento)
    @Query("SELECT a, (a.anoFallecimiento - a.anoNacimiento) as edad FROM AutorEntity a WHERE a.anoNacimiento IS NOT NULL AND a.anoFallecimiento IS NOT NULL ORDER BY (a.anoFallecimiento - a.anoNacimiento) DESC")
    List<Object[]> findAutoresMasLongevos();
}
