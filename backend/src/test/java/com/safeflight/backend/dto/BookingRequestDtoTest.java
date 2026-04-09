package com.safeflight.backend.dto;

import com.safeflight.backend.model.DiscountType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDto_ShouldHaveNoValidationErrors() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setPassengerName("John Doe");
        dto.setPassengerAge(25);
        dto.setExtraBaggage(10);
        dto.setDiscountType(DiscountType.NONE);
        dto.setMilitary(false);

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankPassengerName_ShouldFailValidation() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setPassengerName(""); // invalid
        dto.setPassengerAge(25);

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testNullPassengerAge_ShouldFailValidation() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setPassengerName("John");
        dto.setPassengerAge(null);

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidAge_ShouldFailValidation() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setPassengerName("John");
        dto.setPassengerAge(150); // invalid

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testNegativeExtraBaggage_ShouldFailValidation() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setPassengerName("John");
        dto.setPassengerAge(25);
        dto.setExtraBaggage(-5);

        Set<ConstraintViolation<BookingRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testExtraBaggageSetter_NullShouldDefaultToZero() {
        BookingRequestDto dto = new BookingRequestDto();

        dto.setExtraBaggage(null);

        assertEquals(0, dto.getExtraBaggage());
    }

    @Test
    void testExtraBaggageSetter_ValidValue() {
        BookingRequestDto dto = new BookingRequestDto();

        dto.setExtraBaggage(15);

        assertEquals(15, dto.getExtraBaggage());
    }

    @Test
    void testAllSettersAndGetters() {
        BookingRequestDto dto = new BookingRequestDto();

        dto.setPassengerName("Alice");
        dto.setPassengerAge(30);
        dto.setExtraBaggage(5);
        dto.setReturnFlightId(100L);
        dto.setDiscountType(DiscountType.CHILD);
        dto.setMilitary(true);

        assertEquals("Alice", dto.getPassengerName());
        assertEquals(30, dto.getPassengerAge());
        assertEquals(5, dto.getExtraBaggage());
        assertEquals(100L, dto.getReturnFlightId());
        assertEquals(DiscountType.CHILD, dto.getDiscountType());
        assertTrue(dto.getMilitary());
    }

    @Test
    void testConstructorInitialization() {
        BookingRequestDto dto = new BookingRequestDto(
                "Bob",
                40,
                8,
                DiscountType.SENIOR,
                true
        );

        assertEquals("Bob", dto.getPassengerName());
        assertEquals(40, dto.getPassengerAge());
        assertEquals(8, dto.getExtraBaggage());
        assertEquals(DiscountType.SENIOR, dto.getDiscountType());
        assertTrue(dto.getMilitary());
    }
}