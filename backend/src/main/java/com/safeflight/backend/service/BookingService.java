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

    // Repository for booking database operations
    private  BookingRepo bookingRepository;
    // Repository for flight database operations
    private  FlightRepo flightRepository;
    // Service for fare and price calculations
    private FareService fareService;

    // Constructor to inject repository and service dependencies
    public BookingService(BookingRepo bookingRepository, FlightRepo flightRepository,
            FareService fareService) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.fareService = fareService;
    }

    // Create a new booking with passenger details and calculate total fare
    @Transactional
    public Booking createBooking(User user, Flight flight, BookingRequestDto dto) throws SeatUnavailableException {
        // Check if seats are available on the flight
        if (flight.getAvailableSeats() <= 0) {
            throw new SeatUnavailableException("No seats available on flight " + flight.getFlightNumber());
        }

        // Calculate total fare including taxes, discounts, and baggage charges
        FareBreakdownDto fare = fareService.calculateFare(flight, dto.getExtraBaggage(), dto.getDiscountType());

        // Reduce available seats by 1
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        // Create booking object with all passenger and fare details
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

        // Save booking to database and return
        return bookingRepository.save(booking);
    }

    // Cancel booking, calculate refund, and update flight seat availability
    @Transactional
    public Booking cancelBooking(Long bookingId, User user) {
        // Retrieve booking from database
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found "));

        // Verify user owns this booking
        if (booking == null || booking.getUser() == null || user == null ||
        	    !java.util.Objects.equals(booking.getUser().getId(), user.getId())) {
        	    throw new IllegalArgumentException("You can only cancel your own bookings");
        	}

        // Check if booking is already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("This booking is already cancelled");
        }

        // Calculate days until flight departure
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), booking.getFlight().getDepartureDate());
        // Calculate refund based on days remaining and cancellation policy
        double refund = fareService.calculateRefund(booking.getTotalFare(), daysUntil);
        // Increase available seats on flight by 1
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);
        // Update booking status and set refund amount
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setRefundAmount(refund);
        // Save cancelled booking to database and return
        return bookingRepository.save(booking);
    }

    // Get all upcoming confirmed bookings for a user
    public List<Booking> getUpcomingBookings(User user) {
        // Filter confirmed bookings with departure date in the future
        return bookingRepository.findByUserOrderByBookingDateDesc(user).stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED
                        && !b.getFlight().getDepartureDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    // Get all completed or cancelled bookings for a user
    public List<Booking> getPastBookings(User user) {
        // Filter bookings that are either past departure date or cancelled
        return bookingRepository.findByUserOrderByBookingDateDesc(user).stream()
                .filter(b -> b.getFlight().getDepartureDate().isBefore(LocalDate.now())
                        || b.getStatus() == BookingStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    // Retrieve a specific booking by its ID
    public Booking getBookingById(Long id) {
        // Fetch booking from database, throw exception if not found
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    // Get refund information with amount based on cancellation policy
    public String getRefundInfo(Booking booking) {
        // Calculate days remaining until flight departure
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), booking.getFlight().getDepartureDate());
        // Calculate refund amount based on days
        double refundAmount = fareService.calculateRefund(booking.getTotalFare(), daysUntil);
        // Get refund tier
        String tier = fareService.getRefund(daysUntil);
        // Return formatted refund information string
        return String.format("%s — Estimated Refund: €%.2f", tier, refundAmount);
    }
}



