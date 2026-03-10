package com.safeflight.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safeflight.backend.model.User;

public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
    boolean existsByEmail(String email);
}
