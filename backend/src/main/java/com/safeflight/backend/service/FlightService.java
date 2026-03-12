package com.safeflight.backend.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.FlightRepo;

// Service layer for flight search and retrieval operations
@Service
public class FlightService {
	private FlightRepo flightRepo;
	
	public FlightService(FlightRepo flightRepo) {
		this.flightRepo = flightRepo;
	}
	
	// Search flights by departure date, origin city, and destination city
	public List<Flight> searchFlights(LocalDate date, String fromCity, String toCity) {
        return flightRepo.findByDepartureDateAndFromCityAndToCity(date, fromCity, toCity);
    }
	
	// Retrieve a specific flight by its ID
	public Flight getFlightById(Long id) {
        return flightRepo.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
    }
}
