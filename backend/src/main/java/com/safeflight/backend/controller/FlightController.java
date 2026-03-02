package com.safeflight.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.service.FlightService;

@Controller
public class FlightController {
	public FlightService flightService;
	
	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}
	
	@GetMapping("/flights/search")
    public String searchResults(@RequestParam("date") LocalDate date,
                                @RequestParam("fromCity") String fromCity,
                                @RequestParam("toCity") String toCity,
                                Model model) {
        List<Flight> flights = flightService.searchFlights(date, fromCity, toCity);
        model.addAttribute("flights", flights);
        model.addAttribute("searchDate", date);
        model.addAttribute("fromCity", fromCity);
        model.addAttribute("toCity", toCity);
        return "search-results";
    }
}
