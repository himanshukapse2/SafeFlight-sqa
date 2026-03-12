package com.safeflight.backend.dto;

import com.safeflight.backend.model.DiscountType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO for handling flight booking requests
public class BookingRequestDto {
	@NotBlank(message = "Passenger name is required")
    private String passengerName;

    @NotNull(message = "Passenger age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer passengerAge;

    @NotNull(message = "Extra baggage is required")
    @Min(value = 0, message = "Extra baggage cannot be negative")
    @Max(value = 50, message = "Extra baggage cannot exceed 50 kg")
    private Integer extraBaggage;

    @NotNull(message = "Please select a discount type")
    private DiscountType discountType;

    public BookingRequestDto() {
    }

    public BookingRequestDto(String passengerName, Integer passengerAge, Integer extraBaggage,
            DiscountType discountType) {
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.extraBaggage = extraBaggage;
        this.discountType = discountType;
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
}
