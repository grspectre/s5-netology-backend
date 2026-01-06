package com.utmn.shanaurin.supercomputers.service;

import com.utmn.shanaurin.supercomputers.BaseIntegrationTest;
import com.utmn.shanaurin.supercomputers.repository.SupercomputerJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class DataInitializerServiceTest extends BaseIntegrationTest {

    @Autowired
    private DataInitializerService dataInitializerService;

    @Autowired
    private SupercomputerJpaRepository repository;

    @Test
    void csvDataShouldBeLoaded() {
        // Данные должны быть загружены автоматически при старте контекста
        long count = repository.count();
        assertThat(count).isEqualTo(500); // В CSV 501 записей
    }

    @Test
    void reloadDataFromCsv_shouldReloadData() {
        long initialCount = repository.count();
        dataInitializerService.reloadDataFromCsv();

        long afterReloadCount = repository.count();
        assertThat(afterReloadCount).isEqualTo(initialCount);
    }
}
