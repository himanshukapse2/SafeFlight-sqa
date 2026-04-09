package com.safeflight.backend.service;

import com.safeflight.backend.dto.BookingRequestDto;
import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.exception.ResourceNotFoundException;
import com.safeflight.backend.exception.SeatUnavailableException;
import com.safeflight.backend.model.*;
import com.safeflight.backend.repository.BookingRepo;
import com.safeflight.backend.repository.FlightRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepo bookingRepository;
    @Mock private FlightRepo flightRepository;
    @Mock private FareService fareService;

    @InjectMocks private BookingService bookingService;

    private User user;
    private Flight flight;
    private BookingRequestDto dto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        flight = new Flight();
        flight.setId(10L);
        flight.setAvailableSeats(5);
        flight.setFlightNumber("SF123");
        flight.setDepartureDate(LocalDate.now().plusDays(5));

        dto = new BookingRequestDto();
        dto.setPassengerName("John Doe");
        dto.setExtraBaggage(5);
        dto.setDiscountType(DiscountType.NONE);
    }

    @Test
    void createBooking_Successful() throws SeatUnavailableException {
        FareBreakdownDto fare = new FareBreakdownDto();
        fare.setTotalFare(500.0);
        when(fareService.calculateFare(any(), anyInt(), any())).thenReturn(fare);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        Booking result = bookingService.createBooking(user, flight, dto);

        assertNotNull(result);
        assertEquals(4, flight.getAvailableSeats());
        verify(flightRepository).save(flight);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_NoSeats_ShouldThrowException() {
        flight.setAvailableSeats(0);

        assertThrows(SeatUnavailableException.class, () ->
                bookingService.createBooking(user, flight, dto)
        );
    }

    @Test
    void cancelBooking_Successful() {
        Booking booking = new Booking();
        booking.setId(99L);
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setTotalFare(1000.0);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));
        when(fareService.calculateRefund(anyDouble(), anyLong())).thenReturn(800.0);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Booking cancelled = bookingService.cancelBooking(99L, user);

        assertEquals(BookingStatus.CANCELLED, cancelled.getStatus());
        assertEquals(800.0, cancelled.getRefundAmount());
        assertEquals(6, flight.getAvailableSeats());
    }

    @Test
    void cancelBooking_WrongUser_ShouldThrowException() {
        User otherUser = new User();
        otherUser.setId(2L);

        Booking booking = new Booking();
        booking.setUser(user);

        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.cancelBooking(99L, otherUser)
        );
    }

    @Test
    void getRefundInfo_ShouldReturnFormattedString() {
        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setTotalFare(1000.0);

        when(fareService.calculateRefund(anyDouble(), anyLong())).thenReturn(700.0);
        when(fareService.getRefund(anyLong())).thenReturn("Standard Tier");

        String info = bookingService.getRefundInfo(booking);

        assertTrue(info.contains("Standard Tier"));
        assertTrue(info.contains("€700.00"));
    }

    @Test
    void getUpcomingBookings_ShouldFilterCorrectly() {
        Flight futureFlight = new Flight();
        futureFlight.setDepartureDate(LocalDate.now().plusDays(10));

        Flight pastFlight = new Flight();
        pastFlight.setDepartureDate(LocalDate.now().minusDays(1));

        Booking upcoming = new Booking();
        upcoming.setStatus(BookingStatus.CONFIRMED);
        upcoming.setFlight(futureFlight);

        Booking past = new Booking();
        past.setStatus(BookingStatus.CONFIRMED);
        past.setFlight(pastFlight);

        when(bookingRepository.findByUserOrderByBookingDateDesc(user))
                .thenReturn(java.util.Arrays.asList(upcoming, past));

        List<Booking> result = bookingService.getUpcomingBookings(user);

        assertEquals(1, result.size());
        assertEquals(futureFlight.getDepartureDate(), result.get(0).getFlight().getDepartureDate());
    }

    @Test
    void getPastBookings_ShouldIncludeCancelledAndPastFlights() {
        Flight pastFlight = new Flight();
        pastFlight.setDepartureDate(LocalDate.now().minusDays(5));

        Flight futureFlight = new Flight();
        futureFlight.setDepartureDate(LocalDate.now().plusDays(5));

        Booking completed = new Booking();
        completed.setStatus(BookingStatus.CONFIRMED);
        completed.setFlight(pastFlight);

        Booking cancelledFuture = new Booking();
        cancelledFuture.setStatus(BookingStatus.CANCELLED);
        cancelledFuture.setFlight(futureFlight);

        when(bookingRepository.findByUserOrderByBookingDateDesc(user))
                .thenReturn(java.util.Arrays.asList(completed, cancelledFuture));

        List<Booking> result = bookingService.getPastBookings(user);

        assertEquals(2, result.size());
    }

    @Test
    void getBookingById_ValidId_ShouldReturnBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getBookingById_InvalidId_ShouldThrowException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getBookingById(99L)
        );
    }

}