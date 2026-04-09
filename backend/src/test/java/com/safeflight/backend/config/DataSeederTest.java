package com.safeflight.backend.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.BookingRepo;
import com.safeflight.backend.repository.FlightRepo;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;

class DataSeederTest {

    private DataSeeder dataSeeder;
    private FlightRepo flightRepo;
    private BookingRepo bookingRepo;

    @BeforeEach
    void setUp() {
        dataSeeder = new DataSeeder();
        flightRepo = mock(FlightRepo.class);
        bookingRepo = mock(BookingRepo.class);
    }

    @Test
    void shouldClearAndSeedFlightsWhenForceReloadIsTrueAndNoFlightsExist() throws Exception {
        setForceReload(true);
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(bookingRepo).deleteAll();
        verify(flightRepo).deleteAll();
        verify(flightRepo).saveAll(anyList());
    }

    @Test
    void shouldSeedFlightsWhenDatabaseIsEmpty() throws Exception {
        setForceReload(false);
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(bookingRepo, never()).deleteAll();
        verify(flightRepo, never()).deleteAll();

        ArgumentCaptor<List<Flight>> captor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(captor.capture());

        List<Flight> flights = captor.getValue();
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
        assertGeneratedFlightsAreValid(flights);
    }

    @Test
    void shouldNotSeedFlightsWhenFlightsAlreadyExist() throws Exception {
        setForceReload(false);
        when(flightRepo.count()).thenReturn(5L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(bookingRepo, never()).deleteAll();
        verify(flightRepo, never()).deleteAll();
        verify(flightRepo, never()).saveAll(anyList());
    }

    @Test
    void shouldClearButNotSeedWhenForceReloadIsTrueAndFlightsStillExistAfterClear() throws Exception {
        setForceReload(true);
        when(flightRepo.count()).thenReturn(10L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        verify(bookingRepo).deleteAll();
        verify(flightRepo).deleteAll();
        verify(flightRepo, never()).saveAll(anyList());
    }

    @Test
    void shouldGenerateFlightsForNextEightMonthsWithReturnFlights() throws Exception {
        setForceReload(false);
        when(flightRepo.count()).thenReturn(0L);

        LocalDate beforeRun = LocalDate.now();

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        LocalDate afterRun = LocalDate.now();

        ArgumentCaptor<List<Flight>> captor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(captor.capture());

        List<Flight> flights = captor.getValue();
        assertNotNull(flights);
        assertFalse(flights.isEmpty());

        long expectedMinimumDays = beforeRun.until(beforeRun.plusMonths(8)).getDays();
        assertTrue(flights.size() > 100, "Expected a reasonably large seeded dataset");

        LocalDate minAllowedDate = beforeRun;
        LocalDate maxAllowedDate = afterRun.plusMonths(8).plusDays(5);

        for (Flight flight : flights) {
            assertNotNull(flight);
            assertNotNull(flight.getFlightNumber());
            assertTrue(flight.getFlightNumber().startsWith("SK"));
            assertNotNull(flight.getAirline());
            assertNotNull(flight.getFromCity());
            assertNotNull(flight.getToCity());
            assertNotNull(flight.getDepartureDate());
            assertNotNull(flight.getDepartureTime());
            assertNotNull(flight.getArrivalTime());
            assertTrue(flight.getBasePrice() > 0);
            assertTrue(flight.getAvailableSeats() > 0);

            assertFalse(flight.getDepartureDate().isBefore(minAllowedDate),
                    "Flight date should not be before today");
            assertFalse(flight.getDepartureDate().isAfter(maxAllowedDate),
                    "Flight date should not exceed seeded range");
        }

        assertTrue(expectedMinimumDays >= 0);
    }

    @Test
    void shouldContainBothDomesticAndInternationalFlights() throws Exception {
        setForceReload(false);
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        ArgumentCaptor<List<Flight>> captor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(captor.capture());

        List<Flight> flights = captor.getValue();

        assertTrue(flights.stream().anyMatch(Flight::getDomestic),
                "Expected at least one domestic flight");
        assertTrue(flights.stream().anyMatch(flight -> !flight.getDomestic()),
                "Expected at least one international flight");
    }

    @Test
    void shouldContainKnownRoutes() throws Exception {
        setForceReload(false);
        when(flightRepo.count()).thenReturn(0L);

        CommandLineRunner runner = dataSeeder.loadDummyFlights(flightRepo, bookingRepo);
        runner.run();

        ArgumentCaptor<List<Flight>> captor = ArgumentCaptor.forClass(List.class);
        verify(flightRepo).saveAll(captor.capture());

        List<Flight> flights = captor.getValue();

        assertTrue(flights.stream().anyMatch(f ->
                        "Dublin".equals(f.getFromCity()) && "Cork".equals(f.getToCity())),
                "Expected Dublin to Cork route");

        assertTrue(flights.stream().anyMatch(f ->
                        "Cork".equals(f.getFromCity()) && "Dublin".equals(f.getToCity())),
                "Expected Cork to Dublin route");

        assertTrue(flights.stream().anyMatch(f ->
                        "Dublin".equals(f.getFromCity()) && "Dubai".equals(f.getToCity())),
                "Expected Dublin to Dubai route");

        assertTrue(flights.stream().anyMatch(f ->
                        "Chennai".equals(f.getFromCity()) && "Singapore".equals(f.getToCity())),
                "Expected Chennai to Singapore route");
    }

    private void assertGeneratedFlightsAreValid(List<Flight> flights) {
        LocalDate today = LocalDate.now();
        LocalDate latestAllowed = today.plusMonths(8).plusDays(5);

        for (Flight flight : flights) {
            assertNotNull(flight);
            assertNotNull(flight.getFlightNumber());
            assertTrue(flight.getFlightNumber().startsWith("SK"));

            assertNotNull(flight.getAirline());
            assertFalse(flight.getAirline().isBlank());

            assertNotNull(flight.getFromCity());
            assertFalse(flight.getFromCity().isBlank());

            assertNotNull(flight.getToCity());
            assertFalse(flight.getToCity().isBlank());

            assertNotNull(flight.getDepartureDate());
            assertFalse(flight.getDepartureDate().isBefore(today));
            assertFalse(flight.getDepartureDate().isAfter(latestAllowed));

            assertNotNull(flight.getDepartureTime());
            assertNotNull(flight.getArrivalTime());

            assertTrue(flight.getBasePrice() > 0);
            assertTrue(flight.getAvailableSeats() > 0);
        }
    }

    private void setForceReload(boolean value) throws Exception {
        Field field = DataSeeder.class.getDeclaredField("forceReload");
        field.setAccessible(true);
        field.set(dataSeeder, value);
    }
}