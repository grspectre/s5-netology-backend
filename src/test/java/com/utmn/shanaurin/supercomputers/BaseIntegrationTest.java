package com.utmn.shanaurin.supercomputers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

    /**
     * Один контейнер на всю JVM (singleton).
     * Все тест-классы, наследующие BaseIntegrationTest, будут использовать один и тот же Postgres и порт.
     */
    private static final PostgreSQLContainer<?> POSTGRES = SingletonPostgresContainer.getInstance();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("app.data.init-from-csv", () -> "true");
    }

    /**
     * Singleton-обёртка над PostgreSQLContainer.
     * Важно: стартуем контейнер ровно один раз, и он живёт до завершения JVM (test run).
     */
    static final class SingletonPostgresContainer {

        private static final PostgreSQLContainer<?> INSTANCE =
                new PostgreSQLContainer<>("postgres:16-alpine")
                        .withDatabaseName("supercomputers_test")
                        .withUsername("test")
                        .withPassword("test");

        static {
            INSTANCE.start();
        }

        static PostgreSQLContainer<?> getInstance() {
            return INSTANCE;
        }

        private SingletonPostgresContainer() {
        }
    }
}