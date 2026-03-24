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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
}