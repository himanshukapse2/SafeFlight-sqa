package com.safeflight.backend.service;

import org.springframework.stereotype.Service;

import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.DiscountType;
import com.safeflight.backend.model.Flight;

@Service
public class FareService {
	private static final double TAX_RATE = 0.05; // 5% tax
    private static final double DOMESTIC_BAGGAGE_RATE = 150.0; // ₹150 per kg
    private static final double INTERNATIONAL_BAGGAGE_RATE = 350.0; // ₹350 per kg

    public FareBreakdownDto calculateFare(Flight flight, int extraBaggage, DiscountType discountType) {
        double baseFare = flight.getBasePrice();

        // Baggage charge based on domestic/international
        String baggageModel = flight.getDomestic() ? "Domestic" : "International";
        double baggageRate = flight.getDomestic() ? DOMESTIC_BAGGAGE_RATE : INTERNATIONAL_BAGGAGE_RATE;
        double baggageCharge = extraBaggage * baggageRate;

        // Discount
        double discountAmount = baseFare * discountType.getPercentage() / 100.0;

        // Subtotal before tax
        double subtotal = baseFare + baggageCharge - discountAmount;

        // Tax
        double taxAmount = subtotal * TAX_RATE;

        // Total
        double totalFare = subtotal + taxAmount;

        return FareBreakdownDto.builder()
                .baseFare(round(baseFare))
                .extraBaggage(extraBaggage)
                .baggageCharge(round(baggageCharge))
                .baggageModel(baggageModel)
                .discountLabel(discountType.getLabel())
                .discountPercentage(discountType.getPercentage())
                .discountAmount(round(discountAmount))
                .subtotal(round(subtotal))
                .taxRate(TAX_RATE * 100)
                .taxAmount(round(taxAmount))
                .totalFare(round(totalFare))
                .build();
    }

    public double calculateRefund(double totalFare, long daysUntilDeparture) {
        if (daysUntilDeparture >= 7) {
            return round(totalFare * 0.90);
        } else if (daysUntilDeparture >= 3) {
            return round(totalFare * 0.50);
        } else if (daysUntilDeparture >= 1) {
            return round(totalFare * 0.25);
        } else {
            return 0.0;
        }
    }

    public String getRefund(long daysUntilDeparture) {
        if (daysUntilDeparture >= 7) {
            return "90% Refund (7+ days before departure)";
        } else if (daysUntilDeparture >= 3) {
            return "50% Refund (3-6 days before departure)";
        } else if (daysUntilDeparture >= 1) {
            return "25% Refund (1-2 days before departure)";
        } else {
            return "No Refund (same day cancellation)";
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
