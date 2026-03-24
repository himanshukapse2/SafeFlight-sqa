package com.safeflight.backend.config;

import com.safeflight.backend.repository.FlightRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private FlightRepo flightRepo;

    @InjectMocks
    private DataSeeder dataSeeder;

    @Test
    void loadDummyFlights_EmptyRepo_ShouldSaveFlights() throws Exception {
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo);
        runner.run();

        verify(flightRepo, times(1)).saveAll(anyList());
    }

    @Test
    void loadDummyFlights_PopulatedRepo_ShouldNotSaveFlights() throws Exception {
        when(flightRepo.count()).thenReturn(10L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo);
        runner.run();

        verify(flightRepo, never()).saveAll(anyList());
    }
}