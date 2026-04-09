package com.safeflight.backend.service;

import org.springframework.stereotype.Service;

import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.DiscountType;
import com.safeflight.backend.model.Flight;

// Service layer for calculating fares, taxes, discounts, and refunds
@Service
public class FareService {
	private static final double TAX_RATE = 0.05;
	private static final double DOMESTIC_BAGGAGE_RATE = 1.50;
	private static final double INTERNATIONAL_BAGGAGE_RATE = 3.50;

	// Calculate complete fare breakdown including base fare, baggage, discount, and tax
	public FareBreakdownDto calculateFare(Flight flight, int extraBaggage, DiscountType discountType) {
		double baseFare = flight.getBasePrice();

		String baggageModel = flight.getDomestic() ? "Domestic" : "International";
		double baggageRate = flight.getDomestic() ? DOMESTIC_BAGGAGE_RATE : INTERNATIONAL_BAGGAGE_RATE;
		double baggageCharge = extraBaggage * baggageRate;

		double discountAmount = baseFare * discountType.getPercentage() / 100.0;
		double subtotal = baseFare + baggageCharge - discountAmount;
		double taxAmount = subtotal * TAX_RATE;
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

	// calculate refund amount based on days until departure and cancellation policy
	public double calculateRefund(double totalFare, long daysUntilDeparture) {
		if (daysUntilDeparture >= 7) {
			return round(totalFare * 0.90);
		}
		else if (daysUntilDeparture >= 3) {
			return round(totalFare * 0.50);
		}
		else if (daysUntilDeparture >= 1) {
			return round(totalFare * 0.25);
		}
		else {
			return 0.0;
		}
	}

	// get refund policy description based on days until departure
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
