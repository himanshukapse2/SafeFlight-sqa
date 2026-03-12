package com.safeflight.backend.service;

import org.springframework.stereotype.Service;

import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.DiscountType;
import com.safeflight.backend.model.Flight;

// Service layer for calculating fares, taxes, discounts, and refunds
@Service
public class FareService {
	// Tax percentage applied to all bookings
	private static final double TAX_RATE = 0.05; // 5% tax
	// Baggage cost per kg for domestic flights
	private static final double DOMESTIC_BAGGAGE_RATE = 150.0; // €150 per kg
	// Baggage cost per kg for international flights
	private static final double INTERNATIONAL_BAGGAGE_RATE = 350.0; // €350 per kg

	// Calculate complete fare breakdown including base fare, baggage, discount, and tax
	public FareBreakdownDto calculateFare(Flight flight, int extraBaggage, DiscountType discountType) {
		// Get base fare from flight
		double baseFare = flight.getBasePrice();

		// Determine baggage rate based on flight type
		String baggageModel = flight.getDomestic() ? "Domestic" : "International";
		double baggageRate = flight.getDomestic() ? DOMESTIC_BAGGAGE_RATE : INTERNATIONAL_BAGGAGE_RATE;
		// Calculate total baggage charge
		double baggageCharge = extraBaggage * baggageRate;

		// Calculate discount amount based on passenger category
		double discountAmount = baseFare * discountType.getPercentage() / 100.0;

		// Calculate subtotal before tax (base + baggage - discount)
		double subtotal = baseFare + baggageCharge - discountAmount;
		// Calculate tax on subtotal
		double taxAmount = subtotal * TAX_RATE;

		// Calculate final total fare
		double totalFare = subtotal + taxAmount;
		// Build and return fare breakdown DTO with all details
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

	// Calculate refund amount based on days until departure and cancellation policy
	public double calculateRefund(double totalFare, long daysUntilDeparture) {
		// 90% refund if cancelled 7+ days before departure
		if (daysUntilDeparture >= 7) {
			return round(totalFare * 0.90);
		}
		// 50% refund if cancelled 3-6 days before departure
		else if (daysUntilDeparture >= 3) {
			return round(totalFare * 0.50);
		}
		// 25% refund if cancelled 1-2 days before departure
		else if (daysUntilDeparture >= 1) {
			return round(totalFare * 0.25);
		}
		// No refund for same-day cancellation
		else {
			return 0.0;
		}
	}

	// Get refund policy description based on days until departure
	public String getRefund(long daysUntilDeparture) {
		// Return appropriate refund tier message
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

	// Round monetary values to 2 decimal places
	private double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}
