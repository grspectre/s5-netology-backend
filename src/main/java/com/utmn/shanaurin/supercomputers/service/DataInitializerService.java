package com.utmn.shanaurin.supercomputers.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import com.utmn.shanaurin.supercomputers.repository.SupercomputerJpaRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DataInitializerService {

    private static final Logger log = LoggerFactory.getLogger(DataInitializerService.class);

    private final SupercomputerJpaRepository repository;

    @Value("${app.data.init-from-csv:true}")
    private boolean initFromCsv;

    @Value("${app.data.csv-path:classpath:supercomputers.csv}")
    private Resource csvResource;

    public DataInitializerService(SupercomputerJpaRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (!initFromCsv) {
            log.info("CSV initialization is disabled");
            return;
        }

        long count = repository.count();
        if (count > 0) {
            log.info("Database already contains {} supercomputers, skipping CSV import", count);
            return;
        }

        log.info("Database is empty, starting CSV import...");
        loadDataFromCsv();
    }

    @Transactional
    public void loadDataFromCsv() {
        try {
            List<Supercomputer> supercomputers = parseCsvFile();
            repository.saveAll(supercomputers);
            log.info("Successfully imported {} supercomputers from CSV", supercomputers.size());
        } catch (Exception e) {
            log.error("Failed to import data from CSV", e);
            throw new RuntimeException("CSV import failed", e);
        }
    }

    private List<Supercomputer> parseCsvFile() throws IOException, CsvException {
        List<Supercomputer> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvResource.getInputStream()));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withSkipLines(1) // Пропускаем заголовок
                     .build()) {

            List<String[]> lines = csvReader.readAll();

            for (String[] line : lines) {
                if (line.length < 23) {
                    log.warn("Skipping malformed CSV line with {} columns", line.length);
                    continue;
                }

                Supercomputer sc = new Supercomputer();
//                sc.setId(UUID.randomUUID().toString());
                sc.setRank(parseInteger(line[0]));
                sc.setPreviousRank(parseInteger(line[1]));
                sc.setName(line[2]);
                sc.setSystemModel(line[3]);
                sc.setManufacturer(line[4]);
                sc.setCountry(line[5]);
                sc.setYear(parseInteger(line[6]));
                sc.setSegment(line[7]);
                sc.setTotalCores(parseInteger(line[8]));
                sc.setAcceleratorCores(parseInteger(line[9]));
                sc.setRmaxTflops(parseDouble(line[10]));
                sc.setRpeakTflops(parseDouble(line[11]));
                sc.setPowerKw(parseDouble(line[12]));
                sc.setPowerEfficiency(parseDouble(line[13]));
                sc.setArchitecture(line[14]);
                sc.setProcessorTechnology(line[15]);
                sc.setProcessorSpeedMhz(parseInteger(line[16]));
                sc.setOperatingSystem(line[17]);
                sc.setOsFamily(line[18]);
                sc.setAccelerator(line[19]);
                sc.setCoresPerSocket(parseInteger(line[20]));
                sc.setSystemFamily(line[21]);
                sc.setContinent(line[22]);

                result.add(sc);
            }
        }

        return result;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Принудительная перезагрузка данных из CSV (для административных целей)
     */
    @Transactional
    public void reloadDataFromCsv() {
        log.info("Force reloading data from CSV...");
        repository.deleteAll();
        loadDataFromCsv();
    }
}
