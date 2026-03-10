package com.safeflight.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.FlightRepo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner loadDummyFlights(FlightRepo repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                        new Flight(null, "SK101", "Air India", "Delhi", "Mumbai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(6, 0), LocalTime.of(8, 15), 4500.0, 45, true),
                        new Flight(null, "SK102", "IndiGo", "Delhi", "Mumbai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(9, 30), LocalTime.of(11, 45), 3800.0, 30, true),
                        new Flight(null, "SK103", "SpiceJet", "Delhi", "Mumbai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(14, 0), LocalTime.of(16, 15), 3200.0, 60, true),
                        new Flight(null, "SK104", "Vistara", "Delhi", "Mumbai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(18, 30), LocalTime.of(20, 45), 5200.0, 20, true),
                        new Flight(null, "SK105", "Air India", "Delhi", "Mumbai", LocalDate.of(2026, 3, 10),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4800.0, 50, true),

                        new Flight(null, "SK201", "Air India", "Mumbai", "Delhi", LocalDate.of(2026, 3, 5),
                                LocalTime.of(7, 0), LocalTime.of(9, 15), 4600.0, 48, true),
                        new Flight(null, "SK202", "IndiGo", "Mumbai", "Delhi", LocalDate.of(2026, 3, 5),
                                LocalTime.of(10, 30), LocalTime.of(12, 45), 3900.0, 35, true),

                        new Flight(null, "SK301", "IndiGo", "Bangalore", "Chennai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(6, 30), LocalTime.of(7, 30), 2500.0, 70, true),
                        new Flight(null, "SK401", "Air India", "Chennai", "Bangalore", LocalDate.of(2026, 3, 5),
                                LocalTime.of(7, 0), LocalTime.of(8, 0), 2600.0, 60, true),

                        new Flight(null, "SK701", "Emirates", "Delhi", "Dubai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(2, 0), LocalTime.of(4, 30), 18000.0, 20, false),
                        new Flight(null, "SK702", "Air India", "Delhi", "Dubai", LocalDate.of(2026, 3, 5),
                                LocalTime.of(10, 0), LocalTime.of(12, 30), 15000.0, 30, false),

                        new Flight(null, "SK801", "British Airways", "Mumbai", "London", LocalDate.of(2026, 3, 5),
                                LocalTime.of(1, 0), LocalTime.of(6, 30), 42000.0, 15, false)));
                System.out.println("Dummy flights successfully seeded into the database.");
            }
        };
    }
}
