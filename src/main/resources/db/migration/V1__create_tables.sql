-- Таблица суперкомпьютеров
CREATE TABLE IF NOT EXISTS supercomputer (
                                             id VARCHAR(36) PRIMARY KEY,
    rank INTEGER,
    previous_rank INTEGER,
    name VARCHAR(255),
    system_model VARCHAR(500),
    manufacturer VARCHAR(100),
    country VARCHAR(100),
    year INTEGER,
    segment VARCHAR(50),
    total_cores INTEGER,
    accelerator_cores INTEGER,
    rmax_tflops DOUBLE PRECISION,
    rpeak_tflops DOUBLE PRECISION,
    power_kw DOUBLE PRECISION,
    power_efficiency DOUBLE PRECISION,
    architecture VARCHAR(50),
    processor_technology VARCHAR(100),
    processor_speed_mhz INTEGER,
    operating_system VARCHAR(100),
    os_family VARCHAR(50),
    accelerator VARCHAR(100),
    cores_per_socket INTEGER,
    system_family VARCHAR(100),
    continent VARCHAR(50)
    );

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS person (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    birthday DATE,
    password VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    role VARCHAR(50) DEFAULT 'USER',
    created TIMESTAMP,
    modified TIMESTAMP
    );

-- Индексы для оптимизации запросов
CREATE INDEX IF NOT EXISTS idx_supercomputer_country ON supercomputer(country);
CREATE INDEX IF NOT EXISTS idx_supercomputer_continent ON supercomputer(continent);
CREATE INDEX IF NOT EXISTS idx_supercomputer_rank ON supercomputer(rank);
CREATE INDEX IF NOT EXISTS idx_person_email ON person(email);
