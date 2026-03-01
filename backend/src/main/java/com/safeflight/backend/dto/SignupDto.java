package com.safeflight.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SignupDto {
	
	@NotEmpty(message ="*required")
	private String name;
	
	@NotEmpty(message = "*required")
	private String email;
	
	@NotEmpty(message ="*required")
	@Size(min=6,max =20)
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
}
