package com.safeflight.backend.service;

import com.safeflight.backend.dto.SignupRequestDto;
import com.safeflight.backend.model.Role;
import com.safeflight.backend.model.User;
import com.safeflight.backend.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignupRequestDto signupDto;

    @BeforeEach
    void setUp() {
        signupDto = new SignupRequestDto();
        signupDto.setName("John Doe");
        signupDto.setEmail("john@example.com");
        signupDto.setPassword("plainPassword");
    }

    @Test
    void signUp_Successful() {
        when(userRepo.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_pass");

        doAnswer(returnsFirstArg()).when(userRepo).save(any(User.class));

        User savedUser = userService.signUp(signupDto);

        assertNotNull(savedUser);
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("encoded_pass", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void signUp_UserAlreadyExists_ShouldThrowException() {
        when(userRepo.existsByEmail(anyString())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.signUp(signupDto);
        });

        assertEquals("User Already Exists.", ex.getMessage());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void findByEmail_Success() {
        User mockUser = new User();
        mockUser.setEmail("test@test.com");
        when(userRepo.findByEmail("test@test.com")).thenReturn(mockUser);

        User result = userService.findByEmail("test@test.com");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void findByEmail_NotFound_ShouldThrowRuntimeException() {
        when(userRepo.findByEmail(anyString())).thenThrow(new RuntimeException("DB Error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.findByEmail("notfound@test.com");
        });

        assertEquals("User not found", ex.getMessage());
    }
}