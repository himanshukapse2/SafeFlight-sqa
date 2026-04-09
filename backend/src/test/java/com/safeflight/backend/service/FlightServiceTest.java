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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepo flightRepo;

    @InjectMocks
    private FlightService flightService;

    private Flight matchingFlightOnMinDate;
    private Flight matchingFlightAfterMinDate;
    private Flight wrongRouteFlight;
    private Flight beforeMinDateFlight;

    @BeforeEach
    void setUp() {
        matchingFlightOnMinDate = new Flight();
        matchingFlightOnMinDate.setId(1L);
        matchingFlightOnMinDate.setFromCity("Cork");
        matchingFlightOnMinDate.setToCity("Dublin");
        matchingFlightOnMinDate.setDepartureDate(LocalDate.of(2026, 3, 10));

        matchingFlightAfterMinDate = new Flight();
        matchingFlightAfterMinDate.setId(2L);
        matchingFlightAfterMinDate.setFromCity("Cork");
        matchingFlightAfterMinDate.setToCity("Dublin");
        matchingFlightAfterMinDate.setDepartureDate(LocalDate.of(2026, 3, 12));

        wrongRouteFlight = new Flight();
        wrongRouteFlight.setId(3L);
        wrongRouteFlight.setFromCity("Dublin");
        wrongRouteFlight.setToCity("Cork");
        wrongRouteFlight.setDepartureDate(LocalDate.of(2026, 3, 10));

        beforeMinDateFlight = new Flight();
        beforeMinDateFlight.setId(4L);
        beforeMinDateFlight.setFromCity("Cork");
        beforeMinDateFlight.setToCity("Dublin");
        beforeMinDateFlight.setDepartureDate(LocalDate.of(2026, 3, 8));
    }

    @Test
    void deleteFlight_ShouldCallDeleteById() {
        Long flightId = 10L;

        flightService.deleteFlight(flightId);

        verify(flightRepo, times(1)).deleteById(flightId);
    }

    @Test
    void getReturnFlights_ShouldReturnFlightsMatchingRouteOnOrAfterMinDate() {
        LocalDate minDate = LocalDate.of(2026, 3, 10);

        when(flightRepo.findAll()).thenReturn(List.of(
                matchingFlightOnMinDate,
                matchingFlightAfterMinDate,
                wrongRouteFlight,
                beforeMinDateFlight
        ));

        List<Flight> result = flightService.getReturnFlights("Cork", "Dublin", minDate);

        assertEquals(2, result.size());
        assertTrue(result.contains(matchingFlightOnMinDate));
        assertTrue(result.contains(matchingFlightAfterMinDate));
        assertFalse(result.contains(wrongRouteFlight));
        assertFalse(result.contains(beforeMinDateFlight));

        verify(flightRepo, times(1)).findAll();
    }

    @Test
    void getReturnFlights_ShouldBeCaseInsensitiveForCityNames() {
        LocalDate minDate = LocalDate.of(2026, 3, 10);

        when(flightRepo.findAll()).thenReturn(List.of(
                matchingFlightOnMinDate,
                matchingFlightAfterMinDate
        ));

        List<Flight> result = flightService.getReturnFlights("cOrK", "duBLin", minDate);

        assertEquals(2, result.size());
        assertTrue(result.contains(matchingFlightOnMinDate));
        assertTrue(result.contains(matchingFlightAfterMinDate));
    }

    @Test
    void getReturnFlights_ShouldReturnEmptyListWhenNoFlightsMatch() {
        LocalDate minDate = LocalDate.of(2026, 3, 10);

        when(flightRepo.findAll()).thenReturn(List.of(
                wrongRouteFlight,
                beforeMinDateFlight
        ));

        List<Flight> result = flightService.getReturnFlights("Cork", "Dublin", minDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getReturnFlights_ShouldIncludeFlightExactlyOnMinDate() {
        LocalDate minDate = LocalDate.of(2026, 3, 10);

        when(flightRepo.findAll()).thenReturn(List.of(matchingFlightOnMinDate));

        List<Flight> result = flightService.getReturnFlights("Cork", "Dublin", minDate);

        assertEquals(1, result.size());
        assertEquals(matchingFlightOnMinDate, result.get(0));
    }

    @Test
    void searchFlights_ShouldReturnMatchingFlights() {
        LocalDate date = LocalDate.of(2026, 3, 10);

        Flight flight1 = new Flight();
        flight1.setId(1L);

        Flight flight2 = new Flight();
        flight2.setId(2L);

        when(flightRepo.findByDepartureDateAndFromCityAndToCity(date, "Dublin", "Cork"))
                .thenReturn(List.of(flight1, flight2));

        List<Flight> result = flightService.searchFlights(date, "Dublin", "Cork");

        assertEquals(2, result.size());
        assertEquals(flight1, result.get(0));
        assertEquals(flight2, result.get(1));

        verify(flightRepo, times(1))
                .findByDepartureDateAndFromCityAndToCity(date, "Dublin", "Cork");
    }

    @Test
    void searchFlights_WhenNoFlightsFound_ShouldReturnEmptyList() {
        LocalDate date = LocalDate.of(2026, 3, 10);

        when(flightRepo.findByDepartureDateAndFromCityAndToCity(date, "Dublin", "Cork"))
                .thenReturn(List.of());

        List<Flight> result = flightService.searchFlights(date, "Dublin", "Cork");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(flightRepo).findByDepartureDateAndFromCityAndToCity(date, "Dublin", "Cork");
    }

    @Test
    void getFlightById_WhenFlightExists_ShouldReturnFlight() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightRepo.findById(1L)).thenReturn(java.util.Optional.of(flight));

        Flight result = flightService.getFlightById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(flightRepo).findById(1L);
    }

    @Test
    void getFlightById_WhenFlightNotFound_ShouldThrowException() {
        when(flightRepo.findById(1L)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            flightService.getFlightById(1L);
        });

        assertEquals("Flight not found", exception.getMessage());

        verify(flightRepo).findById(1L);
    }
}