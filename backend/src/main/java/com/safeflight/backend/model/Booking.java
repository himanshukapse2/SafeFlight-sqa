package com.safeflight.backend.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Entity class representing a flight booking
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(nullable = false)
    private String passengerName;

    @Column(nullable = false)
    private Integer passengerAge;

    @Column(nullable = false)
    private Integer extraBaggage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private Double totalFare;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    private Double baseFare;
    private Double baggageCharge;
    private Double discountAmount;
    private Double taxAmount;
    private Double refundAmount;

    public Booking() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getPassengerAge() {
        return passengerAge;
    }

    public void setPassengerAge(Integer passengerAge) {
        this.passengerAge = passengerAge;
    }

    public Integer getExtraBaggage() {
        return extraBaggage;
    }

    public void setExtraBaggage(Integer extraBaggage) {
        this.extraBaggage = extraBaggage;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getBaggageCharge() {
        return baggageCharge;
    }

    public void setBaggageCharge(Double baggageCharge) {
        this.baggageCharge = baggageCharge;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    // Builder
    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private final Booking b = new Booking();

        public BookingBuilder user(User u) {
            b.user = u;
            return this;
        }

        public BookingBuilder flight(Flight f) {
            b.flight = f;
            return this;
        }

        public BookingBuilder passengerName(String v) {
            b.passengerName = v;
            return this;
        }

        public BookingBuilder passengerAge(Integer v) {
            b.passengerAge = v;
            return this;
        }

        public BookingBuilder extraBaggage(Integer v) {
            b.extraBaggage = v;
            return this;
        }

        public BookingBuilder discountType(DiscountType v) {
            b.discountType = v;
            return this;
        }

        public BookingBuilder status(BookingStatus v) {
            b.status = v;
            return this;
        }

        public BookingBuilder totalFare(Double v) {
            b.totalFare = v;
            return this;
        }

        public BookingBuilder bookingDate(LocalDateTime v) {
            b.bookingDate = v;
            return this;
        }

        public BookingBuilder baseFare(Double v) {
            b.baseFare = v;
            return this;
        }

        public BookingBuilder baggageCharge(Double v) {
            b.baggageCharge = v;
            return this;
        }

        public BookingBuilder discountAmount(Double v) {
            b.discountAmount = v;
            return this;
        }

        public BookingBuilder taxAmount(Double v) {
            b.taxAmount = v;
            return this;
        }

        public BookingBuilder refundAmount(Double v) {
            b.refundAmount = v;
            return this;
        }

        public Booking build() {
            return b;
        }
    }
}

