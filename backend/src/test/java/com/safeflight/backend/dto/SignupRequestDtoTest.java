package com.safeflight.backend.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.Arguments;

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

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidSignupRequests")
    void testInvalidDto_ShouldFailValidation(
            String testName,
            String name,
            String email,
            String password) {

        SignupRequestDto dto = new SignupRequestDto();
        dto.setName(name);
        dto.setEmail(email);
        dto.setPassword(password);

        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    private static Stream<Arguments> invalidSignupRequests() {
        return Stream.of(
                arguments("Blank name", "", "john@example.com", "secure123"),
                arguments("Invalid email", "John", "invalid-email", "secure123"),
                arguments("Blank email", "John", "", "secure123"),
                arguments("Short password", "John", "john@example.com", "123"),
                arguments("Blank password", "John", "john@example.com", "")
        );
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