package com.utmn.shanaurin.supercomputers.service;

import com.utmn.shanaurin.supercomputers.model.Supercomputer;
import com.utmn.shanaurin.supercomputers.repository.SupercomputerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupercomputerServiceTest {

    @Mock
    private SupercomputerJpaRepository repository;

    @InjectMocks
    private SupercomputerService service;

    private Supercomputer fugaku;
    private Supercomputer summit;

    @BeforeEach
    void setUp() {
        fugaku = new Supercomputer();
        fugaku.setId("id-1");
        fugaku.setName("Fugaku");
        fugaku.setCountry("Japan");
        fugaku.setContinent("Asia");
        fugaku.setRmaxTflops(442010.0);

        summit = new Supercomputer();
        summit.setId("id-2");
        summit.setName("Summit");
        summit.setCountry("United States");
        summit.setContinent("North America");
        summit.setRmaxTflops(148600.0);
    }

    @Test
    void getAll_shouldReturnAllSupercomputers() {
        when(repository.findAll()).thenReturn(List.of(fugaku, summit));

        List<Supercomputer> result = (List<Supercomputer>) service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Supercomputer::getName).containsExactly("Fugaku", "Summit");
        verify(repository).findAll();
    }

    @Test
    void getOne_existingId_shouldReturnSupercomputer() {
        when(repository.findById("id-1")).thenReturn(Optional.of(fugaku));

        Supercomputer result = service.getOne("id-1");

        assertThat(result.getName()).isEqualTo("Fugaku");
        verify(repository).findById("id-1");
    }

    @Test
    void getOne_nonExistingId_shouldThrowNotFoundException() {
        when(repository.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOne("non-existent"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void add_shouldSaveAndReturnSupercomputer() {
        Supercomputer newSc = new Supercomputer();
        newSc.setName("New Supercomputer");

        when(repository.save(any())).thenAnswer(invocation -> {
            Supercomputer saved = invocation.getArgument(0);
            saved.setId("generated-id");
            return saved;
        });

        Supercomputer result = service.add(newSc);

        assertThat(result.getId()).isEqualTo("generated-id");
        assertThat(result.getName()).isEqualTo("New Supercomputer");
        verify(repository).save(any());
    }

    @Test
    void update_existingId_shouldUpdate() {
        when(repository.existsById("id-1")).thenReturn(true);
        when(repository.save(fugaku)).thenReturn(fugaku);

        service.update(fugaku);

        verify(repository).existsById("id-1");
        verify(repository).save(fugaku);
    }

    @Test
    void update_nonExistingId_shouldThrowNotFoundException() {
        Supercomputer sc = new Supercomputer();
        sc.setId("non-existent");

        when(repository.existsById("non-existent")).thenReturn(false);

        assertThatThrownBy(() -> service.update(sc))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("не найден");

        verify(repository, never()).save(any());
    }

    @Test
    void delete_existingId_shouldDelete() {
        when(repository.existsById("id-1")).thenReturn(true);
        doNothing().when(repository).deleteById("id-1");

        service.delete("id-1");

        verify(repository).existsById("id-1");
        verify(repository).deleteById("id-1");
    }

    @Test
    void delete_nonExistingId_shouldThrowNotFoundException() {
        when(repository.existsById("non-existent")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("non-existent"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("не найден");

        verify(repository, never()).deleteById(any());
    }

    @Test
    void avgPerformance_shouldReturnAverage() {
        when(repository.getAvgPerformance()).thenReturn(295305.0);

        Double result = service.avgPerformance();

        assertThat(result).isEqualTo(295305.0);
        verify(repository).getAvgPerformance();
    }

    @Test
    void getByCountry_shouldReturnFilteredList() {
        when(repository.findByCountryIgnoreCase("Japan")).thenReturn(List.of(fugaku));

        List<Supercomputer> result = service.getByCountry("Japan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Fugaku");
    }

    @Test
    void countByContinent_shouldReturnMap() {
        when(repository.countByContinent()).thenReturn(List.of(
                new Object[]{"Asia", 5L},
                new Object[]{"North America", 3L}
        ));

        var result = service.countByContinent();

        assertThat(result).containsEntry("Asia", 5L);
        assertThat(result).containsEntry("North America", 3L);
    }
}
