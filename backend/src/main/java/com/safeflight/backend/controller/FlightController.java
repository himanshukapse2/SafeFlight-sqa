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
	private final FlightService flightService;

	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}

	// Display the main flight search page
	@GetMapping("/")
	public String searchPage(Model model) {
		// Provide today's date as the minimum selectable date for the date picker
		model.addAttribute("minDate", LocalDate.now().toString());
		// Ensure template has these attributes (may be empty) to avoid NPEs
		model.addAttribute("dateValue", null);
		model.addAttribute("dateError", null);
		return "search";
	}

	// Search flights based on date, departure city, and destination city
	@GetMapping("/flights/search")
	public String searchFlights(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("fromCity") String fromCity,
			@RequestParam("toCity") String toCity,
			Model model) {

        fromCity = (fromCity != null && !fromCity.isEmpty())
                ? fromCity.substring(0, 1).toUpperCase() + fromCity.substring(1).toLowerCase()
                : fromCity;

        toCity = (toCity != null && !toCity.isEmpty())
                ? toCity.substring(0, 1).toUpperCase() + toCity.substring(1).toLowerCase()
                : toCity;

		// Server-side validation: do not allow searching for past dates
		LocalDate today = LocalDate.now();
		if (date.isBefore(today)) {
			model.addAttribute("dateError", "Travel date cannot be in the past.");
			// Preserve entered values so user doesn't have to re-type
			model.addAttribute("dateValue", date.toString());
			model.addAttribute("fromCity", fromCity);
			model.addAttribute("toCity", toCity);
			// ensure the template still has minDate
			model.addAttribute("minDate", LocalDate.now().toString());
			return "search";
		}

		List<Flight> flights = flightService.searchFlights(date, fromCity, toCity);
		model.addAttribute("flights", flights);
		model.addAttribute("searchDate", date);
		model.addAttribute("fromCity", fromCity);
		model.addAttribute("toCity", toCity);
		return "search-results";
	}
}
