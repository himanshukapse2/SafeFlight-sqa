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
	
	// Delete flight by id
    public void deleteFlight(Long id) {
        flightRepo.deleteById(id);
    }
    
    // Find return flights (from destination back to origin, on or after departure date)
    public List<Flight> getReturnFlights(String fromCity, String toCity, LocalDate minDate) {
        return flightRepo.findAll().stream()
                .filter(f -> f.getFromCity().equalsIgnoreCase(fromCity) 
                          && f.getToCity().equalsIgnoreCase(toCity)
                          && (f.getDepartureDate().isEqual(minDate) || f.getDepartureDate().isAfter(minDate)))
                .toList();
    }
}
