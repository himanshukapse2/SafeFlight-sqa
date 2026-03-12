package com.safeflight.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.FlightRepo;

// Service layer for flight search and retrieval operations
@Service
public class FlightService {
	// Repository for flight database operations
	private FlightRepo flightRepo;
	
	// Constructor to inject FlightRepository dependency
	public FlightService(FlightRepo flightRepo) {
		this.flightRepo = flightRepo;
	}
	
	// Search flights by departure date, origin city, and destination city
	public List<Flight> searchFlights(LocalDate date, String fromCity, String toCity) {
		// Query database for matching flights
        return flightRepo.findByDepartureDateAndFromCityAndToCity(date, fromCity, toCity);
    }
	
	// Retrieve a specific flight by its ID
	public Flight getFlightById(Long id) {
		// Fetch flight from database, throw exception if not found
        return flightRepo.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
    }
}
