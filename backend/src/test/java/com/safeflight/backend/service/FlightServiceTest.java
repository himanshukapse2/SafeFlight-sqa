package com.safeflight.backend.service;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.FlightRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepo flightRepo;

    @InjectMocks
    private FlightService flightService;

    private Flight mockFlight;

    @BeforeEach
    void setUp() {
        mockFlight = new Flight();
        mockFlight.setId(1L);
        mockFlight.setFlightNumber("SF101");
        mockFlight.setFromCity("Delhi");
        mockFlight.setToCity("Mumbai");
        mockFlight.setDepartureDate(LocalDate.now());
    }

    @Test
    void searchFlights_ShouldReturnList() {
        LocalDate date = LocalDate.now();
        when(flightRepo.findByDepartureDateAndFromCityAndToCity(date, "Delhi", "Mumbai"))
                .thenReturn(Arrays.asList(mockFlight));

        List<Flight> results = flightService.searchFlights(date, "Delhi", "Mumbai");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("SF101", results.get(0).getFlightNumber());
    }

    @Test
    void getFlightById_ValidId_ShouldReturnFlight() {
        when(flightRepo.findById(1L)).thenReturn(Optional.of(mockFlight));

        Flight result = flightService.getFlightById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getFlightById_InvalidId_ShouldThrowException() {
        when(flightRepo.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                flightService.getFlightById(99L)
        );

        assertEquals("Flight not found", exception.getMessage());
    }
}
