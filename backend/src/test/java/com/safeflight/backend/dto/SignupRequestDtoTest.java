package com.safeflight.backend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDto_ShouldPassValidation() {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPassword("secure123");

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankName_ShouldFailValidation() {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setName("");
        dto.setEmail("john@example.com");
        dto.setPassword("secure123");

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidEmail_ShouldFailValidation() {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setName("John");
        dto.setEmail("invalid-email"); // invalid
        dto.setPassword("secure123");

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testBlankEmail_ShouldFailValidation() {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setName("John");
        dto.setEmail("");
        dto.setPassword("secure123");

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testShortPassword_ShouldFailValidation() {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPassword("123"); // too short

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testBlankPassword_ShouldFailValidation() {
        SignupRequestDto dto = new SignupRequestDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPassword("");

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testConstructorInitialization() {
        SignupRequestDto dto = new SignupRequestDto(
                "Alice",
                "alice@example.com",
                "password123"
        );

        assertEquals("Alice", dto.getName());
        assertEquals("alice@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
    }

    @Test
    void testSettersAndGetters() {
        SignupRequestDto dto = new SignupRequestDto();

        dto.setName("Bob");
        dto.setEmail("bob@example.com");
        dto.setPassword("secure456");

        assertEquals("Bob", dto.getName());
        assertEquals("bob@example.com", dto.getEmail());
        assertEquals("secure456", dto.getPassword());
    }
}