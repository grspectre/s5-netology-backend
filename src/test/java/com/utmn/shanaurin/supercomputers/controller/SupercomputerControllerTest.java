package com.utmn.shanaurin.supercomputers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import com.utmn.shanaurin.supercomputers.repository.SupercomputerJpaRepository;
import com.utmn.shanaurin.supercomputers.service.SupercomputerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SupercomputerController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(SupercomputerService.class)
class SupercomputerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SupercomputerJpaRepository repository;

    @Test
    void add_shouldReturnCreated() throws Exception {
        Supercomputer newSc = new Supercomputer();
        newSc.setName("Test Supercomputer");
        newSc.setCountry("Russia");
        newSc.setRmaxTflops(1000.5);

        Supercomputer saved = new Supercomputer();
        saved.setId("generated-id");
        saved.setName("Test Supercomputer");
        saved.setCountry("Russia");
        saved.setRmaxTflops(1000.5);

        given(repository.save(any())).willReturn(saved);

        mockMvc.perform(post("/api/supercomputers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSc)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("generated-id")))
                .andExpect(jsonPath("$.name", is("Test Supercomputer")));
    }

    @Test
    void getOne_existingId_shouldReturnSupercomputer() throws Exception {
        Supercomputer sc = new Supercomputer();
        sc.setId("test-id-001");
        sc.setName("Fugaku");
        sc.setCountry("Japan");
        sc.setRmaxTflops(442010.0);

        given(repository.findById("test-id-001")).willReturn(Optional.of(sc));

        mockMvc.perform(get("/api/supercomputers/test-id-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Fugaku")))
                .andExpect(jsonPath("$.country", is("Japan")))
                .andExpect(jsonPath("$.rmaxTflops", is(442010.0)));
    }

    @Test
    void getOne_nonExistingId_shouldReturn404() throws Exception {
        given(repository.findById("non-existent")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/supercomputers/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existingId_shouldReturn204() throws Exception {
        given(repository.existsById("test-id")).willReturn(true);

        mockMvc.perform(delete("/api/supercomputers/test-id"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_nonExistingId_shouldReturn404() throws Exception {
        given(repository.existsById("non-existent")).willReturn(false);

        mockMvc.perform(delete("/api/supercomputers/non-existent"))
                .andExpect(status().isNotFound());
    }
}
