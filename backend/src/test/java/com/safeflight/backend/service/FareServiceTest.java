package com.safeflight.backend.service;

import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.DiscountType;
import com.safeflight.backend.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FareServiceTest {

    private FareService fareService;
    private Flight domesticFlight;
    private Flight internationalFlight;

    @BeforeEach
    void setUp() {
        fareService = new FareService();

        domesticFlight = new Flight();
        domesticFlight.setBasePrice(100.0);
        domesticFlight.setDomestic(true);

        internationalFlight = new Flight();
        internationalFlight.setBasePrice(100.0);
        internationalFlight.setDomestic(false);
    }

    @Test
    void calculateFare_Domestic_NoDiscount() {
        FareBreakdownDto result = fareService.calculateFare(domesticFlight, 5, DiscountType.NONE);

        assertEquals(100.0, result.getBaseFare());
        assertEquals(7.5, result.getBaggageCharge());
        assertEquals("Domestic", result.getBaggageModel());
        assertEquals(112.88, result.getTotalFare());
    }

    @Test
    void calculateFare_International_WithDiscount() {
        FareBreakdownDto result = fareService.calculateFare(internationalFlight, 2, DiscountType.SENIOR);

        assertEquals(7.0, result.getBaggageCharge());
        assertEquals("International", result.getBaggageModel());
        assertEquals(101.85, result.getTotalFare());
    }

    @Test
    void calculateRefund_Tiers() {
        double fare = 1000.0;
        assertEquals(900.0, fareService.calculateRefund(fare, 10));
        assertEquals(500.0, fareService.calculateRefund(fare, 4));
        assertEquals(250.0, fareService.calculateRefund(fare, 1));
        assertEquals(0.0, fareService.calculateRefund(fare, 0));
    }

    @Test
    void getRefund_Descriptions() {
        assertTrue(fareService.getRefund(10).contains("90%"));
        assertTrue(fareService.getRefund(4).contains("50%"));
        assertTrue(fareService.getRefund(1).contains("25%"));
        assertTrue(fareService.getRefund(0).contains("No Refund"));
    }
}