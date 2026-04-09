package com.safeflight.backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareBreakdownDtoTest {

    @Test
    void testSettersAndGetters() {
        FareBreakdownDto dto = new FareBreakdownDto();

        dto.setBaseFare(1000.0);
        dto.setExtraBaggage(5);
        dto.setBaggageCharge(200.0);
        dto.setBaggageModel("Domestic");
        dto.setDiscountLabel("Student");
        dto.setDiscountPercentage(10);
        dto.setDiscountAmount(100.0);
        dto.setSubtotal(1100.0);
        dto.setTaxRate(5.0);
        dto.setTaxAmount(55.0);
        dto.setTotalFare(1155.0);

        assertEquals(1000.0, dto.getBaseFare());
        assertEquals(5, dto.getExtraBaggage());
        assertEquals(200.0, dto.getBaggageCharge());
        assertEquals("Domestic", dto.getBaggageModel());
        assertEquals("Student", dto.getDiscountLabel());
        assertEquals(10, dto.getDiscountPercentage());
        assertEquals(100.0, dto.getDiscountAmount());
        assertEquals(1100.0, dto.getSubtotal());
        assertEquals(5.0, dto.getTaxRate());
        assertEquals(55.0, dto.getTaxAmount());
        assertEquals(1155.0, dto.getTotalFare());
    }

    @Test
    void testBuilderPattern() {
        FareBreakdownDto dto = FareBreakdownDto.builder()
                .baseFare(2000.0)
                .extraBaggage(10)
                .baggageCharge(300.0)
                .baggageModel("International")
                .discountLabel("Senior Citizen")
                .discountPercentage(15)
                .discountAmount(300.0)
                .subtotal(2000.0)
                .taxRate(5.0)
                .taxAmount(100.0)
                .totalFare(2100.0)
                .build();

        assertNotNull(dto);
        assertEquals(2000.0, dto.getBaseFare());
        assertEquals(10, dto.getExtraBaggage());
        assertEquals(300.0, dto.getBaggageCharge());
        assertEquals("International", dto.getBaggageModel());
        assertEquals("Senior Citizen", dto.getDiscountLabel());
        assertEquals(15, dto.getDiscountPercentage());
        assertEquals(300.0, dto.getDiscountAmount());
        assertEquals(2000.0, dto.getSubtotal());
        assertEquals(5.0, dto.getTaxRate());
        assertEquals(100.0, dto.getTaxAmount());
        assertEquals(2100.0, dto.getTotalFare());
    }

    @Test
    void testDefaultConstructor() {
        FareBreakdownDto dto = new FareBreakdownDto();
        assertNotNull(dto);
    }

    @Test
    void testBuilderPartialValues() {
        FareBreakdownDto dto = FareBreakdownDto.builder()
                .baseFare(1500.0)
                .totalFare(1600.0)
                .build();

        assertEquals(1500.0, dto.getBaseFare());
        assertEquals(1600.0, dto.getTotalFare());

        // Unset values should remain null/default
        assertNull(dto.getDiscountLabel());
        assertEquals(0, dto.getDiscountPercentage()); // primitive int default
    }
}