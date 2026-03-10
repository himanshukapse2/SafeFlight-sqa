package com.safeflight.backend.service;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.safeflight.backend.dto.SignupRequestDto;
import com.safeflight.backend.model.Role;
import com.safeflight.backend.model.User;
import com.safeflight.backend.repository.UserRepo;

@Service
public class UserService {
	private UserRepo userRepo;
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = 	passwordEncoder;
	}
	
	public User signUp(SignupRequestDto dto) {
		if(userRepo.existsByEmail(dto.getEmail())) {
			throw new IllegalArgumentException("User Already Exists.");
		}
		
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(Role.USER);
		
		return userRepo.save(user);
	}
	
	public User findByEmail(String email) {
		try {
			return userRepo.findByEmail(email);
		}
		catch(RuntimeException e){
			throw new RuntimeException("User not found");
		}
	}
}
