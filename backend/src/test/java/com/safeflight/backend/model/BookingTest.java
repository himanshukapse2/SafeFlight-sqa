package com.safeflight.backend.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setId(1L);

        Flight flight = new Flight();
        flight.setId(10L);

        Booking booking = new Booking();

        booking.setId(100L);
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setPassengerName("John Doe");
        booking.setPassengerAge(30);
        booking.setExtraBaggage(5);
        booking.setDiscountType(DiscountType.NONE);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalFare(5000.0);
        booking.setBookingDate(LocalDateTime.now());
        booking.setBaseFare(4000.0);
        booking.setBaggageCharge(200.0);
        booking.setDiscountAmount(100.0);
        booking.setTaxAmount(300.0);
        booking.setRefundAmount(0.0);

        assertEquals(100L, booking.getId());
        assertEquals(user, booking.getUser());
        assertEquals(flight, booking.getFlight());
        assertEquals("John Doe", booking.getPassengerName());
        assertEquals(30, booking.getPassengerAge());
        assertEquals(5, booking.getExtraBaggage());
        assertEquals(DiscountType.NONE, booking.getDiscountType());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(5000.0, booking.getTotalFare());
        assertNotNull(booking.getBookingDate());
        assertEquals(4000.0, booking.getBaseFare());
        assertEquals(200.0, booking.getBaggageCharge());
        assertEquals(100.0, booking.getDiscountAmount());
        assertEquals(300.0, booking.getTaxAmount());
        assertEquals(0.0, booking.getRefundAmount());
    }

    @Test
    void testBuilderPattern() {
        User user = new User();
        user.setId(1L);

        Flight flight = new Flight();
        flight.setId(10L);

        LocalDateTime now = LocalDateTime.now();

        Booking booking = Booking.builder()
                .user(user)
                .flight(flight)
                .passengerName("Alice")
                .passengerAge(25)
                .extraBaggage(3)
                .discountType(DiscountType.CHILD)
                .status(BookingStatus.CONFIRMED)
                .totalFare(3000.0)
                .bookingDate(now)
                .baseFare(2500.0)
                .baggageCharge(150.0)
                .discountAmount(200.0)
                .taxAmount(250.0)
                .refundAmount(0.0)
                .build();

        assertNotNull(booking);
        assertEquals(user, booking.getUser());
        assertEquals(flight, booking.getFlight());
        assertEquals("Alice", booking.getPassengerName());
        assertEquals(25, booking.getPassengerAge());
        assertEquals(3, booking.getExtraBaggage());
        assertEquals(DiscountType.CHILD, booking.getDiscountType());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(3000.0, booking.getTotalFare());
        assertEquals(now, booking.getBookingDate());
        assertEquals(2500.0, booking.getBaseFare());
        assertEquals(150.0, booking.getBaggageCharge());
        assertEquals(200.0, booking.getDiscountAmount());
        assertEquals(250.0, booking.getTaxAmount());
        assertEquals(0.0, booking.getRefundAmount());
    }

    @Test
    void testDefaultConstructor() {
        Booking booking = new Booking();
        assertNotNull(booking);
    }
}