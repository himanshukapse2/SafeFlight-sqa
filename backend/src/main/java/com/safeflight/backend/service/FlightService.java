package com.safeflight.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.repository.FlightRepo;

@Service
public class FlightService {
	private FlightRepo flightRepo;
	
	public FlightService(FlightRepo flightRepo) {
		this.flightRepo = flightRepo;
	}
	
	public List<Flight> searchFlights(LocalDate date, String fromCity, String toCity) {
        return flightRepo.findByDepartureDateAndFromCityAndToCity(date, fromCity, toCity);
    }
	
	public Flight getFlightById(Long id) {
        return flightRepo.findById(id).orElseThrow(() -> new RuntimeException("Flight not found"));
    }
}
