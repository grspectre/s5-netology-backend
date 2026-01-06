package com.utmn.shanaurin.supercomputers.repository;

import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SupercomputerJpaRepository extends CrudRepository<Supercomputer, String> {

    // Native SQL
    @Query(value = "SELECT AVG(s.rmax_tflops) FROM supercomputer s", nativeQuery = true)
    Double avgPerformance();

    // JPQL
    @Query("SELECT AVG(s.rmaxTflops) FROM Supercomputer s")
    Double getAvgPerformance();

    // Найти по стране
    List<Supercomputer> findByCountryIgnoreCase(String country);

    // Найти по континенту
    List<Supercomputer> findByContinentIgnoreCase(String continent);

    // Топ N по производительности
    @Query("SELECT s FROM Supercomputer s ORDER BY s.rmaxTflops DESC")
    List<Supercomputer> findTopByPerformance();

    // Средняя мощность
    @Query("SELECT AVG(s.powerKw) FROM Supercomputer s WHERE s.powerKw IS NOT NULL")
    Double getAvgPower();

    // Количество по континентам
    @Query("SELECT s.continent, COUNT(s) FROM Supercomputer s GROUP BY s.continent")
    List<Object[]> countByContinent();
}
