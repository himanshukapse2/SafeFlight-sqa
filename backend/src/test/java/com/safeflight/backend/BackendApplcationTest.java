package com.safeflight.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BackendApplicationTest {

    @Test
    void contextLoads() {
        // Agar Spring context successfully load ho gaya,
        // test pass ho jayega
    }

    @Test
    void mainMethod_ShouldRunWithoutException() {
        assertDoesNotThrow(() -> BackendApplication.main(new String[]{}));
    }
}