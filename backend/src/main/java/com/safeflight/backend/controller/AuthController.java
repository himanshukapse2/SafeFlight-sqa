package com.safeflight.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.safeflight.backend.dto.SignupRequestDto;
import com.safeflight.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

/*
 * AuthController handles login/signup HTTP requests for the application.
 * Provides endpoints for user login and signup functionality.
 * Endpoints:-
 * - GET /login - Display login page
 * - GET /signup - Display signup page with empty form
 * - POST /signup - Process user registration with validation
 */
@Controller
public class AuthController {

	//Service layer dependency
	private final UserService userService;

	/*
	 * Constructor for dependency injection of UserService
	 * @param userService the service layer for user management operations
	 */
	public AuthController(UserService userService) {
		this.userService = userService;
	}


	//Displays the login page for existing users
	@GetMapping("/login")
	public String loginPage() {
		// Return the login.html template
		return "login";
	}

	//Displays the signup/registration page with an empty signup form
	@GetMapping("/signup")
	public String signupPage(Model model) {
		// Add an empty SignupRequestDto to the model for form binding
		// This allows Thymeleaf to bind form input fields to the DTO properties
		model.addAttribute("signupRequest", new SignupRequestDto());
		// Return the signup.html Thymeleaf template
		return "signup";
	}
	

	 //Processes user registration/signup with form data validation
	@PostMapping("/signup")
	public String signup(@Valid @ModelAttribute("signupRequest") SignupRequestDto dto,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		// Check if there are any validation errors from the @Valid annotation
		// Validation includes checking email format, password strength, required fields, etc.
		if (result.hasErrors()) {
			// If validation fails, return to signup page with error messages
			// Spring automatically binds error messages to the view
			return "signup";
		}

		try {
			// Attempt to register the user via UserService
			userService.signUp(dto);

			// This message will be displayed on the login page after redirect
			redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please login.");
			// Redirect to login page after successful signup
			return "redirect:/login";
			
		} catch (IllegalArgumentException e) {
			// Catch business logic exceptions
			// Add the error message to the model to display on the signup page
			model.addAttribute("errorMessage", e.getMessage());
			// Return to signup page with error message displayed
			return "signup";
		}
	}
}
