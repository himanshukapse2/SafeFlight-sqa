package com.safeflight.backend.config;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.BookingRepo;
import com.safeflight.backend.repository.FlightRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private FlightRepo flightRepo;

    @Mock
    private BookingRepo bookingRepo;

    @InjectMocks
    private DataSeeder dataSeeder;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dataSeeder, "forceReload", false);
    }

    @Test
    void loadDummyFlights_WhenForceReloadEnabled_ShouldClearDatabaseAndSeedFlights() throws Exception {
        ReflectionTestUtils.setField(dataSeeder, "forceReload", true);
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(bookingRepo).deleteAll();
        verify(flightRepo).deleteAll();
        verify(flightRepo).count();
        verify(flightRepo).saveAll(anyList());
    }

    @Test
    void loadDummyFlights_WhenNoFlightsExist_ShouldSeedFlights() throws Exception {
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        ArgumentCaptor<List<Flight>> flightsCaptor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(flightsCaptor.capture());

        List<Flight> seededFlights = flightsCaptor.getValue();

        assertNotNull(seededFlights);
        assertFalse(seededFlights.isEmpty());
        assertEquals(154, seededFlights.size());

        Flight firstFlight = seededFlights.get(0);
        assertEquals("SK1001", firstFlight.getFlightNumber());
        assertEquals("Aer lingus", firstFlight.getAirline());
        assertEquals("Dublin", firstFlight.getFromCity());
        assertEquals("Cork", firstFlight.getToCity());
        assertEquals(LocalDate.of(2026, 3, 10), firstFlight.getDepartureDate());
        assertTrue(firstFlight.getDomestic());

        verify(bookingRepo, never()).deleteAll();
        verify(flightRepo, never()).deleteAll();
    }

    @Test
    void loadDummyFlights_WhenFlightsAlreadyExist_ShouldNotSeedFlights() throws Exception {
        when(flightRepo.count()).thenReturn(5L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(flightRepo).count();
        verify(flightRepo, never()).saveAll(anyList());
        verify(bookingRepo, never()).deleteAll();
        verify(flightRepo, never()).deleteAll();
    }

    @Test
    void loadDummyFlights_WhenForceReloadEnabledAndFlightsAlreadyExist_ShouldClearAndReseed() throws Exception {
        ReflectionTestUtils.setField(dataSeeder, "forceReload", true);
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(bookingRepo).deleteAll();
        verify(flightRepo).deleteAll();
        verify(flightRepo).saveAll(anyList());
    }

    @Test
    void loadDummyFlights_ShouldContainDomesticAndInternationalFlights() throws Exception {
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        ArgumentCaptor<List<Flight>> flightsCaptor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(flightsCaptor.capture());

        List<Flight> seededFlights = flightsCaptor.getValue();

        assertTrue(seededFlights.stream().anyMatch(Flight::getDomestic));
        assertTrue(seededFlights.stream().anyMatch(flight -> !flight.getDomestic()));
    }

    @Test
    void loadDummyFlights_ShouldContainExpectedSampleRoutes() throws Exception {
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        ArgumentCaptor<List<Flight>> flightsCaptor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(flightsCaptor.capture());

        List<Flight> seededFlights = flightsCaptor.getValue();

        assertTrue(seededFlights.stream().anyMatch(flight ->
                "Dublin".equals(flight.getFromCity()) &&
                        "Cork".equals(flight.getToCity())
        ));

        assertTrue(seededFlights.stream().anyMatch(flight ->
                "Dublin".equals(flight.getFromCity()) &&
                        "Dubai".equals(flight.getToCity())
        ));

        assertTrue(seededFlights.stream().anyMatch(flight ->
                "Chennai".equals(flight.getFromCity()) &&
                        "Singapore".equals(flight.getToCity())
        ));

        assertTrue(seededFlights.stream().anyMatch(flight ->
                "Cork".equals(flight.getFromCity()) &&
                        "Dublin".equals(flight.getToCity()) &&
                        LocalDate.of(2026, 3, 15).equals(flight.getDepartureDate())
        ));
    }
}