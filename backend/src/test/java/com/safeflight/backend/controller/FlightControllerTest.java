package com.safeflight.backend.controller;

import com.safeflight.backend.model.Flight;
import com.safeflight.backend.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightService flightService;

    @Test
    @WithMockUser
    void searchPage_ShouldReturnSearchView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    @WithMockUser
    void searchFlights_ShouldReturnResultsView() throws Exception {
        Flight f1 = new Flight();
        f1.setId(1L);
        f1.setFlightNumber("SF123");
        f1.setAvailableSeats(10);
        f1.setBasePrice(200.0);
        f1.setDomestic(true);

        when(flightService.searchFlights(any(), anyString(), anyString()))
                .thenReturn(Arrays.asList(f1));

        mockMvc.perform(get("/flights/search")
                        .param("date", "2026-03-23")
                        .param("fromCity", "Delhi")
                        .param("toCity", "Mumbai"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    @WithMockUser
    void searchFlights_EmptyResults_ShouldStillWork() throws Exception {
        when(flightService.searchFlights(any(), anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/flights/search")
                        .param("date", LocalDate.now().toString())
                        .param("fromCity", "X")
                        .param("toCity", "Y"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("flights", Collections.emptyList()));
    }

    @Test
    @WithMockUser
    void searchFlights_RoundTripWithReturnDateBeforeTravelDate_ShouldReturnSearchWithError() throws Exception {
        LocalDate departureDate = LocalDate.now().plusDays(5);
        LocalDate returnDate = LocalDate.now().plusDays(3);

        mockMvc.perform(get("/flights/search")
                        .param("date", departureDate.toString())
                        .param("returnDate", returnDate.toString())
                        .param("tripType", "roundTrip")
                        .param("fromCity", "dublin")
                        .param("toCity", "cork"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attribute("dateError", "Return date cannot be before travel date."))
                .andExpect(model().attribute("dateValue", departureDate.toString()))
                .andExpect(model().attribute("tripType", "roundTrip"))
                .andExpect(model().attribute("returnDateValue", returnDate.toString()))
                .andExpect(model().attribute("fromCity", "Dublin"))
                .andExpect(model().attribute("toCity", "Cork"))
                .andExpect(model().attributeExists("minDate"));

        verify(flightService, never()).searchFlights(any(), anyString(), anyString());
    }

    @Test
    @WithMockUser
    void searchFlights_RoundTripWithValidReturnDate_ShouldAddReturnFlightsToModel() throws Exception {
        LocalDate departureDate = LocalDate.now().plusDays(5);
        LocalDate returnDate = LocalDate.now().plusDays(8);

        Flight outboundFlight = new Flight();
        outboundFlight.setId(1L);
        outboundFlight.setFlightNumber("SF101");
        outboundFlight.setAirline("Aer Lingus");
        outboundFlight.setFromCity("Dublin");
        outboundFlight.setToCity("Cork");
        outboundFlight.setDepartureDate(departureDate);
        outboundFlight.setBasePrice(4500.0);
        outboundFlight.setAvailableSeats(20);
        outboundFlight.setDomestic(true);

        Flight inboundFlight = new Flight();
        inboundFlight.setId(2L);
        inboundFlight.setFlightNumber("SF102");
        inboundFlight.setAirline("Etihad");
        inboundFlight.setFromCity("Cork");
        inboundFlight.setToCity("Dublin");
        inboundFlight.setDepartureDate(returnDate);
        inboundFlight.setBasePrice(4700.0);
        inboundFlight.setAvailableSeats(18);
        inboundFlight.setDomestic(true);

        when(flightService.searchFlights(departureDate, "Dublin", "Cork"))
                .thenReturn(List.of(outboundFlight));
        when(flightService.searchFlights(returnDate, "Cork", "Dublin"))
                .thenReturn(List.of(inboundFlight));

        mockMvc.perform(get("/flights/search")
                        .param("date", departureDate.toString())
                        .param("returnDate", returnDate.toString())
                        .param("tripType", "roundTrip")
                        .param("fromCity", "dublin")
                        .param("toCity", "cork"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-results"))
                .andExpect(model().attributeExists("flights"))
                .andExpect(model().attributeExists("returnFlights"))
                .andExpect(model().attribute("searchDate", departureDate))
                .andExpect(model().attribute("returnDate", returnDate))
                .andExpect(model().attribute("fromCity", "Dublin"))
                .andExpect(model().attribute("toCity", "Cork"))
                .andExpect(model().attribute("tripType", "roundTrip"));

        verify(flightService).searchFlights(departureDate, "Dublin", "Cork");
        verify(flightService).searchFlights(returnDate, "Cork", "Dublin");
    }
}