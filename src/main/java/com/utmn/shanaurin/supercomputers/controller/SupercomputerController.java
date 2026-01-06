package com.utmn.shanaurin.supercomputers.controller;

import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import com.utmn.shanaurin.supercomputers.service.SupercomputerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supercomputers")
@Tag(name = "Supercomputers", description = "API для управления суперкомпьютерами")
public class SupercomputerController {

    private final SupercomputerService service;

    public SupercomputerController(SupercomputerService service) {
        this.service = service;
    }

    @Operation(summary = "Получить все суперкомпьютеры",
            description = "Возвращает список всех суперкомпьютеров. Может работать медленно при большом объёме данных.")
    @GetMapping
    public Iterable<Supercomputer> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Получить суперкомпьютер по ID")
    @GetMapping("/{id}")
    public Supercomputer getOne(
            @Parameter(description = "Уникальный идентификатор суперкомпьютера")
            @PathVariable("id") String id
    ) {
        return service.getOne(id);
    }

    @Operation(summary = "Добавить новый суперкомпьютер")
    @PostMapping
    public ResponseEntity<Supercomputer> add(@RequestBody Supercomputer supercomputer) {
        Supercomputer created = service.add(supercomputer);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить суперкомпьютер")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    public void update(@RequestBody Supercomputer supercomputer) {
        service.update(supercomputer);
    }

    @Operation(summary = "Удалить суперкомпьютер")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "Уникальный идентификатор суперкомпьютера")
            @PathVariable("id") String id
    ) {
        service.delete(id);
    }

    @Operation(summary = "Средняя производительность",
            description = "Возвращает среднее значение Rmax (TFlop/s) по всем суперкомпьютерам")
    @GetMapping("/stats/avg-performance")
    public Double avgPerformance() {
        return service.avgPerformance();
    }

    @Operation(summary = "Средняя мощность",
            description = "Возвращает среднее потребление энергии (кВт)")
    @GetMapping("/stats/avg-power")
    public Double avgPower() {
        return service.avgPower();
    }

    @Operation(summary = "Получить по стране")
    @GetMapping("/country/{country}")
    public List<Supercomputer> getByCountry(
            @Parameter(description = "Название страны")
            @PathVariable("country") String country
    ) {
        return service.getByCountry(country);
    }

    @Operation(summary = "Получить по континенту")
    @GetMapping("/continent/{continent}")
    public List<Supercomputer> getByContinent(
            @Parameter(description = "Название континента")
            @PathVariable("continent") String continent
    ) {
        return service.getByContinent(continent);
    }

    @Operation(summary = "Статистика по континентам",
            description = "Возвращает количество суперкомпьютеров по континентам")
    @GetMapping("/stats/by-continent")
    public Map<String, Long> countByContinent() {
        return service.countByContinent();
    }
}
