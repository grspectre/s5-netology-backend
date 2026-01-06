package com.utmn.shanaurin.supercomputers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utmn.shanaurin.supercomputers.BaseIntegrationTest;
import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import com.utmn.shanaurin.supercomputers.repository.SupercomputerJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class SupercomputerControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SupercomputerJpaRepository repository;

    @Test
    @WithMockUser(roles = "USER")
    void getAllSupercomputers_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/supercomputers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByCountry_shouldReturnFilteredList() throws Exception {
        mockMvc.perform(get("/api/supercomputers/country/Japan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].country", everyItem(equalToIgnoringCase("Japan"))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getByContinent_shouldReturnFilteredList() throws Exception {
        mockMvc.perform(get("/api/supercomputers/continent/Asia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].continent", everyItem(equalToIgnoringCase("Asia"))));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAvgPerformance_shouldReturnValue() throws Exception {
        mockMvc.perform(get("/api/supercomputers/stats/avg-performance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", greaterThan(0.0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getCountByContinent_shouldReturnMap() throws Exception {
        mockMvc.perform(get("/api/supercomputers/stats/by-continent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Asia", greaterThan(0)))
                .andExpect(jsonPath("$.Europe", greaterThan(0)));
    }

    @Test
    @Transactional
    @WithMockUser(roles = "USER")
    void createSupercomputer_shouldReturn201() throws Exception {
        Supercomputer newSc = new Supercomputer();
        newSc.setName("Test Supercomputer");
        newSc.setCountry("Russia");
        newSc.setContinent("Europe");
        newSc.setManufacturer("Test Corp");
        newSc.setRmaxTflops(1000.0);

        mockMvc.perform(post("/api/supercomputers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSc)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Supercomputer"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSupercomputerById_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/supercomputers/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteSupercomputer_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/supercomputers/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void accessWithoutAuth_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/supercomputers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getSupercomputerById_shouldReturnSupercomputer() throws Exception {
        // Получаем первый суперкомпьютер из базы
        Supercomputer first = repository.findAll().iterator().next();

        mockMvc.perform(get("/api/supercomputers/{id}", first.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(first.getId()))
                .andExpect(jsonPath("$.name").value(first.getName()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateSupercomputer_shouldReturn204() throws Exception {
        // Получаем суперкомпьютер для обновления
        Supercomputer existing = repository.findAll().iterator().next();
        existing.setName("Updated Name");

        mockMvc.perform(put("/api/supercomputers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existing)))
                .andExpect(status().isNoContent());
    }
}
