package com.safeflight.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.service.FlightService;

@Controller
public class FlightController {
	// Service for flight search and retrieval operations
	private final FlightService flightService;

	// Constructor to inject FlightService dependency
	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}

	// Display the main flight search page
	@GetMapping("/")
	public String searchPage() {
		// Return search form page
		return "search";
	}

	// Search flights based on date, departure city, and destination city
	@GetMapping("/flights/search")
	public String searchFlights(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("fromCity") String fromCity,
			@RequestParam("toCity") String toCity,
			Model model) {

		// Query database for matching flights
		List<Flight> flights = flightService.searchFlights(date, fromCity, toCity);
		// Add search results to model
		model.addAttribute("flights", flights);
		// Add search parameters for display on results page
		model.addAttribute("searchDate", date);
		model.addAttribute("fromCity", fromCity);
		model.addAttribute("toCity", toCity);
		// Return results page
		return "search-results";
	}
}
