package com.safeflight.backend.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.safeflight.backend.model.User;
import com.safeflight.backend.repository.UserRepo;

// Service for Spring Security user authentication and authorization
@Service
public class CustomUserDetailService implements UserDetailsService {

	// Repository for user database operations
	private final UserRepo userRepository;

	// Constructor to inject UserRepository dependency
	public CustomUserDetailService(UserRepo userRepository) {
		this.userRepository = userRepository;
	}

	// Load user details from database for authentication
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Find user by email in database
		User user = userRepository.findByEmail(email);
		// Throw exception if user not found
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		// Create Spring Security user object with email, password, and default role
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				// Assign ROLE_USER to all users
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	}
}


