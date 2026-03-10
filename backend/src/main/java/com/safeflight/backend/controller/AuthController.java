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

@Controller
public class AuthController {

	 private final UserService userService;

	    public AuthController(UserService userService) {
	        this.userService = userService;
	    }

	    @GetMapping("/login")
	    public String loginPage() {
	        return "login";
	    }

	    @GetMapping("/signup")
	    public String signupPage(Model model) {
	        model.addAttribute("signupRequest", new SignupRequestDto());
	        return "signup";
	    }

	    @PostMapping("/signup")
	    public String signup(@Valid @ModelAttribute("signupRequest") SignupRequestDto dto,
	            BindingResult result,
	            Model model,
	            RedirectAttributes redirectAttributes) {
	        if (result.hasErrors()) {
	            return "signup";
	        }

	        try {
	            userService.signUp(dto);
	            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please login.");
	            return "redirect:/login";
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("errorMessage", e.getMessage());
	            return "signup";
	        }
	    }
}
