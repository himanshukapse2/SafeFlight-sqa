package com.safeflight.backend.service;

import com.safeflight.backend.model.User;
import com.safeflight.backend.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private CustomUserDetailService userDetailService;

    @Test
    void loadUserByUsername_Success() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encoded_password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        UserDetails userDetails = userDetailService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encoded_password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound_ShouldThrowException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailService.loadUserByUsername("notfound@example.com")
        );
    }
}