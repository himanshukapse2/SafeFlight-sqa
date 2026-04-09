package com.safeflight.backend.config;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.BookingRepo;
import com.safeflight.backend.repository.FlightRepo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
Loads dummy flight data into the database at application startup.
Configured in application.properties, the below command loads the data in local database when the application is run for the first time.
spring.jpa.hibernate.ddl-auto=update
*/
@Configuration
public class DataSeeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSeeder.class);

    private static final String SEED_FORCE_RELOAD_PROPERTY = "${app.seed-data.force-reload:false}";
    private static final String MSG_FORCE_RELOAD = "Force reload is enabled. Clearing database...";
    private static final String MSG_SEEDED = "Dummy flights successfully seeded into the database.";
    private static final String MSG_ALREADY_EXISTS = "Flights already exist. No seeding required.";

    private static final String DUBLIN = "Dublin";
    private static final String CORK = "Cork";
    private static final String BANGALORE = "Bangalore";
    private static final String CHENNAI = "Chennai";
    private static final String HYDERABAD = "Hyderabad";
    private static final String LONDON = "London";
    private static final String DUBAI = "Dubai";
    private static final String SINGAPORE = "Singapore";
    private static final String GOA = "Goa";

    private static final String AER_LINGUS = "Aer Lingus";
    private static final String ETIHAD = "Etihad";
    private static final String TURKISH = "Turkish";
    private static final String QATAR = "Qatar";
    private static final String EMIRATES = "Emirates";
    private static final String SINGAPORE_AIRLINES = "Singapore Airlines";
    private static final String BRITISH = "British Airways";

    private static final int FLIGHT_CODE_MIN = 1000;
    private static final int FLIGHT_CODE_BOUND = 9000;
    private static final int MIN_SEATS = 12;
    private static final int MAX_SEATS_EXCLUSIVE = 71;
    private static final int SEED_MONTHS_AHEAD = 8;
    private static final int RETURN_FLIGHT_OFFSET_DAYS = 5;

    private final Random random = new Random();

    @Value(SEED_FORCE_RELOAD_PROPERTY)
    private boolean forceReload;

    @Bean
    public CommandLineRunner loadDummyFlights(FlightRepo flightRepo, BookingRepo bookingRepo) {
        return args -> {
            if (forceReload) {
                LOGGER.info(MSG_FORCE_RELOAD);
                bookingRepo.deleteAll();
                flightRepo.deleteAll();
            }

            if (flightRepo.count() == 0) {
                flightRepo.saveAll(generateFlights());
                LOGGER.info(MSG_SEEDED);
            } else {
                LOGGER.info(MSG_ALREADY_EXISTS);
            }
        };
    }

    private List<Flight> generateFlights() {
        List<Flight> flights = new ArrayList<>();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(SEED_MONTHS_AHEAD);

        while (!startDate.isAfter(endDate)) {
            addDailyFlights(flights, startDate);

            if (shouldAddReturnFlights(startDate)) {
                addReturnFlights(flights, startDate.plusDays(RETURN_FLIGHT_OFFSET_DAYS));
            }

            startDate = startDate.plusDays(1);
        }

        return flights;
    }

    private void addDailyFlights(List<Flight> flights, LocalDate date) {
        flights.add(flight(code(), AER_LINGUS, DUBLIN, CORK, date, t(6, 0), t(8, 15), randPrice(4400, 5200), randSeats(), true));
        flights.add(flight(code(), ETIHAD, DUBLIN, CORK, date, t(9, 30), t(11, 45), randPrice(3600, 4600), randSeats(), true));
        flights.add(flight(code(), TURKISH, DUBLIN, CORK, date, t(14, 0), t(16, 15), randPrice(3000, 3900), randSeats(), true));
        flights.add(flight(code(), QATAR, DUBLIN, CORK, date, t(18, 30), t(20, 45), randPrice(5000, 5900), randSeats(), true));

        flights.add(flight(code(), AER_LINGUS, CORK, DUBLIN, date, t(7, 0), t(9, 15), randPrice(4400, 5300), randSeats(), true));
        flights.add(flight(code(), ETIHAD, CORK, DUBLIN, date, t(10, 30), t(12, 45), randPrice(3600, 4700), randSeats(), true));

        flights.add(flight(code(), ETIHAD, BANGALORE, CHENNAI, date, t(6, 30), t(7, 30), randPrice(2200, 3200), randSeats(), true));
        flights.add(flight(code(), AER_LINGUS, CHENNAI, BANGALORE, date, t(7, 0), t(8, 0), randPrice(2300, 3300), randSeats(), true));
        flights.add(flight(code(), ETIHAD, BANGALORE, HYDERABAD, date, t(8, 0), t(9, 15), randPrice(2500, 3500), randSeats(), true));
        flights.add(flight(code(), AER_LINGUS, HYDERABAD, BANGALORE, date, t(10, 15), t(11, 30), randPrice(2600, 3600), randSeats(), true));

        flights.add(flight(code(), QATAR, DUBLIN, BANGALORE, date, t(6, 45), t(9, 30), randPrice(5200, 6200), randSeats(), true));
        flights.add(flight(code(), ETIHAD, BANGALORE, DUBLIN, date, t(19, 0), t(21, 45), randPrice(4800, 5800), randSeats(), true));

        flights.add(flight(code(), AER_LINGUS, CORK, GOA, date, t(11, 0), t(12, 30), randPrice(3300, 4300), randSeats(), true));
        flights.add(flight(code(), ETIHAD, GOA, CORK, date, t(13, 15), t(14, 45), randPrice(3200, 4200), randSeats(), true));

        flights.add(flight(code(), EMIRATES, DUBLIN, DUBAI, date, t(2, 0), t(4, 30), randPrice(16000, 22000), randSeats(), false));
        flights.add(flight(code(), AER_LINGUS, DUBLIN, DUBAI, date, t(10, 0), t(12, 30), randPrice(14000, 19000), randSeats(), false));
        flights.add(flight(code(), BRITISH, CORK, LONDON, date, t(1, 0), t(6, 30), randPrice(39000, 46000), randSeats(), false));
        flights.add(flight(code(), AER_LINGUS, DUBLIN, LONDON, date, t(3, 0), t(8, 15), randPrice(37000, 44000), randSeats(), false));
        flights.add(flight(code(), SINGAPORE_AIRLINES, CHENNAI, SINGAPORE, date, t(23, 15), t(3, 30), randPrice(21000, 26000), randSeats(), false));
        flights.add(flight(code(), ETIHAD, CORK, DUBAI, date, t(21, 0), t(23, 45), randPrice(15000, 20000), randSeats(), false));
    }

    private void addReturnFlights(List<Flight> flights, LocalDate date) {
        flights.add(flight(code(), AER_LINGUS, CORK, DUBLIN, date, t(7, 0), t(9, 15), randPrice(4400, 5300), randSeats(), true));
        flights.add(flight(code(), ETIHAD, CORK, DUBLIN, date, t(10, 30), t(12, 45), randPrice(3600, 4700), randSeats(), true));
    }

    private boolean shouldAddReturnFlights(LocalDate date) {
        return date.getDayOfMonth() % 3 == 0;
    }

    private Flight flight(
            String code,
            String airline,
            String source,
            String destination,
            LocalDate date,
            LocalTime departureTime,
            LocalTime arrivalTime,
            double price,
            int seats,
            boolean domestic
    ) {
        return new Flight(
                null,
                code,
                airline,
                source,
                destination,
                date,
                departureTime,
                arrivalTime,
                price,
                seats,
                domestic
        );
    }

    private String code() {
        return "SK" + (FLIGHT_CODE_MIN + random.nextInt(FLIGHT_CODE_BOUND));
    }

    private double randPrice(int minInclusive, int maxExclusive) {
        return minInclusive + random.nextInt(maxExclusive - minInclusive);
    }

    private int randSeats() {
        return MIN_SEATS + random.nextInt(MAX_SEATS_EXCLUSIVE - MIN_SEATS);
    }

    private LocalTime t(int hour, int minute) {
        return LocalTime.of(hour, minute);
    }
}