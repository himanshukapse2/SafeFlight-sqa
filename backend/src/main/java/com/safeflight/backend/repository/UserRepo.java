package com.safeflight.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.safeflight.backend.model.User;

//Repository interface for UserRepo
public interface UserRepo extends JpaRepository<User, Long>{
	User findByEmail(String email);
    boolean existsByEmail(String email);
}
