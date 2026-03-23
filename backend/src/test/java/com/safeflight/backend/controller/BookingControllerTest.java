package com.safeflight.backend.controller;

import com.safeflight.backend.config.SecurityConfig;
import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.Booking;
import com.safeflight.backend.model.Flight;
import com.safeflight.backend.model.User;
import com.safeflight.backend.service.BookingService;
import com.safeflight.backend.service.FareService;
import com.safeflight.backend.service.FlightService;
import com.safeflight.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookingController.class)
@Import(SecurityConfig.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;
    @MockitoBean
    private FlightService flightService;
    @MockitoBean
    private FareService fareService;
    @MockitoBean
    private UserService userService;

    private Flight mockFlight;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setName("John Doe");

        mockFlight = new Flight();
        mockFlight.setId(1L);
        mockFlight.setFlightNumber("SF101");
        mockFlight.setBasePrice(500.0);
        mockFlight.setDomestic(true);
        mockFlight.setAvailableSeats(10);
        mockFlight.setDepartureDate(LocalDate.now().plusDays(2));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void bookingForm_ShouldReturnBookView() throws Exception {
        when(flightService.getFlightById(1L)).thenReturn(mockFlight);

        mockMvc.perform(get("/bookings/book/1").with(user("user@test.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attributeExists("flight", "bookingRequest"));
    }

    @Test
    void createBooking_Successful_ShouldRedirect() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        Flight mockFlight = new Flight();
        mockFlight.setId(1L);
        mockFlight.setAvailableSeats(10);
        mockFlight.setDomestic(true);
        mockFlight.setBasePrice(100.0);

        Booking mockBooking = new Booking();
        mockBooking.setId(100L);

        FareBreakdownDto mockFare = new FareBreakdownDto();
        mockFare.setTotalFare(150.0);

        when(userService.findByEmail(anyString())).thenReturn(mockUser);
        when(flightService.getFlightById(anyLong())).thenReturn(mockFlight);
        when(bookingService.createBooking(any(), any(), any())).thenReturn(mockBooking);
        when(fareService.calculateFare(any(), anyInt(), any())).thenReturn(mockFare);

        mockMvc.perform(post("/bookings/book/1")
                        .with(csrf())
                        .with(user("test@example.com"))
                        .param("passengerName", "John Doe")
                        .param("passengerAge", "25")
                        .param("extraBaggage", "5")
                        .param("discountType", "NONE"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/my"));
    }


    @Test
    @WithMockUser(username = "test@example.com")
    void createBooking_ValidationError_ShouldReturnForm() throws Exception {
        when(flightService.getFlightById(anyLong())).thenReturn(mockFlight);

        mockMvc.perform(post("/bookings/book/1")
                        .with(csrf())
                        .with(user("test@example.com"))
                        .param("passengerName", "")
                        .param("passengerAge", "25")
                        .param("extraBaggage", "5")
                        .param("discountType", "NONE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().hasErrors());
    }


    @Test
    @WithMockUser(username = "user@test.com")
    void myBookings_ShouldReturnView() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(mockUser);
        when(bookingService.getUpcomingBookings(any())).thenReturn(Collections.emptyList());
        when(bookingService.getPastBookings(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings/my").with(user("user@test.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("my-bookings"))
                .andExpect(model().attributeExists("upcomingBookings", "pastBookings"));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void cancelBooking_Successful_ShouldRedirect() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@test.com");

        Booking cancelledBooking = new Booking();
        cancelledBooking.setId(101L);
        cancelledBooking.setRefundAmount(50.0);

        when(userService.findByEmail(anyString())).thenReturn(mockUser);
        when(bookingService.cancelBooking(anyLong(), any())).thenReturn(cancelledBooking);

        mockMvc.perform(post("/bookings/cancel/1")
                        .with(csrf())
                        .with(user("user@test.com")))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/my"))
                .andExpect(flash().attributeExists("successMessage"));
    }


    @Test
    @WithMockUser(username = "user@test.com")
    void cancelConfirm_ValidUser_ShouldReturnCancelView() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@test.com");

        Flight mockFlight = new Flight();
        mockFlight.setFlightNumber("SF-123");

        Booking mockBooking = new Booking();
        mockBooking.setId(99L);
        mockBooking.setUser(mockUser);
        mockBooking.setFlight(mockFlight);
        mockBooking.setTotalFare(5000.0);

        when(userService.findByEmail("user@test.com")).thenReturn(mockUser);
        when(bookingService.getBookingById(99L)).thenReturn(mockBooking);
        when(bookingService.getRefundInfo(any())).thenReturn("Refund: €50.00");

        mockMvc.perform(get("/bookings/cancel/99").with(user("user@test.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("cancel-confirm"))
                .andExpect(model().attributeExists("booking", "refundInfo"));
    }

    @Test
    @WithMockUser(username = "attacker@test.com")
    void cancelConfirm_DifferentUser_ShouldThrowException() throws Exception {
        User attacker = new User();
        attacker.setId(2L);
        attacker.setEmail("attacker@test.com");

        User owner = new User();
        owner.setId(1L);

        Booking othersBooking = new Booking();
        othersBooking.setId(99L);
        othersBooking.setUser(owner);

        when(userService.findByEmail("attacker@test.com")).thenReturn(attacker);
        when(bookingService.getBookingById(99L)).thenReturn(othersBooking);

        mockMvc.perform(get("/bookings/cancel/99")
                        .with(user("attacker@test.com")))
                .andExpect(status().isOk());
    }

}