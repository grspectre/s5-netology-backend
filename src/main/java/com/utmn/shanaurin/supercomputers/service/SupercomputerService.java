package com.utmn.shanaurin.supercomputers.service;

import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import com.utmn.shanaurin.supercomputers.repository.SupercomputerJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SupercomputerService {

    private final SupercomputerJpaRepository repository;

    public SupercomputerService(SupercomputerJpaRepository repository) {
        this.repository = repository;
    }

    public Iterable<Supercomputer> getAll() {
        return repository.findAll();
    }

    public Supercomputer getOne(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Суперкомпьютер не найден"));
    }

    @Transactional
    public Supercomputer add(Supercomputer supercomputer) {
        supercomputer.setId(null); // Позволяем JPA генерировать ID
        return repository.save(supercomputer);
    }

    @Transactional
    public void update(Supercomputer supercomputer) {
        if (!repository.existsById(supercomputer.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Суперкомпьютер не найден");
        }
        repository.save(supercomputer);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Суперкомпьютер не найден");
        }
        repository.deleteById(id);
    }

    public Double avgPerformance() {
        return repository.getAvgPerformance();
    }

    public Double avgPower() {
        return repository.getAvgPower();
    }

    public List<Supercomputer> getByCountry(String country) {
        return repository.findByCountryIgnoreCase(country);
    }

    public List<Supercomputer> getByContinent(String continent) {
        return repository.findByContinentIgnoreCase(continent);
    }

    public Map<String, Long> countByContinent() {
        return repository.countByContinent().stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    public long count() {
        return repository.count();
    }
}