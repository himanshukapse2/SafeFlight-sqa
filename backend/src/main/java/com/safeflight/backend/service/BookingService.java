package com.safeflight.backend.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safeflight.backend.dto.BookingRequestDto;
import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.Booking;
import com.safeflight.backend.model.BookingStatus;
import com.safeflight.backend.model.Flight;
import com.safeflight.backend.model.User;
import com.safeflight.backend.repository.BookingRepo;
import com.safeflight.backend.repository.FlightRepo;
import com.safeflight.backend.exception.ResourceNotFoundException;
import com.safeflight.backend.exception.SeatUnavailableException;

import jakarta.transaction.Transactional;

// Service layer for managing all booking-related business logic
@Service
public class BookingService {

    private BookingRepo bookingRepository;
    private FlightRepo flightRepository;
    private FareService fareService;

    public BookingService(BookingRepo bookingRepository, FlightRepo flightRepository,
            FareService fareService) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.fareService = fareService;
    }

    // Create a new booking with passenger details and calculate total fare
    @Transactional
    public Booking createBooking(User user, Flight flight, BookingRequestDto dto) throws SeatUnavailableException {
        if (flight.getAvailableSeats() <= 0) {
            throw new SeatUnavailableException("No seats available on flight " + flight.getFlightNumber());
        }
        FareBreakdownDto fare = fareService.calculateFare(flight, dto.getExtraBaggage(), dto.getDiscountType());
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = Booking.builder()
                .user(user)
                .flight(flight)
                .passengerName(dto.getPassengerName())
                .passengerAge(dto.getPassengerAge())
                .extraBaggage(dto.getExtraBaggage())
                .discountType(dto.getDiscountType())
                .status(BookingStatus.CONFIRMED)
                .totalFare(fare.getTotalFare())
                .bookingDate(LocalDateTime.now())
                .baseFare(fare.getBaseFare())
                .baggageCharge(fare.getBaggageCharge())
                .discountAmount(fare.getDiscountAmount())
                .taxAmount(fare.getTaxAmount())
                .build();

        return bookingRepository.save(booking);
    }

    // Cancel booking, calculate refund, and update flight seat availability
    @Transactional
    public Booking cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found "));

        if (booking == null || booking.getUser() == null || user == null ||
        	    !java.util.Objects.equals(booking.getUser().getId(), user.getId())) {
        	    throw new IllegalArgumentException("You can only cancel your own bookings");
            }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("This booking is already cancelled");
        }

        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), booking.getFlight().getDepartureDate());
        double refund = fareService.calculateRefund(booking.getTotalFare(), daysUntil);

        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setRefundAmount(refund);
        return bookingRepository.save(booking);
    }

    // Get all upcoming confirmed bookings for a user
    public List<Booking> getUpcomingBookings(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user).stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED
                        && !b.getFlight().getDepartureDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    // Get all completed or cancelled bookings for a user
    public List<Booking> getPastBookings(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user).stream()
                .filter(b -> b.getFlight().getDepartureDate().isBefore(LocalDate.now())
                        || b.getStatus() == BookingStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    // Retrieve a specific booking by its ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    // Get refund information with amount based on cancellation policy
    public String getRefundInfo(Booking booking) {
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), booking.getFlight().getDepartureDate());
        double refundAmount = fareService.calculateRefund(booking.getTotalFare(), daysUntil);
        String tier = fareService.getRefund(daysUntil);
        return String.format("%s — Estimated Refund: €%.2f", tier, refundAmount);
    }
}
