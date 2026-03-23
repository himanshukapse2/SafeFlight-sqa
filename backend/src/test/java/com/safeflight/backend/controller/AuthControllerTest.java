package com.safeflight.backend.controller;

import com.safeflight.backend.dto.SignupRequestDto;
import com.safeflight.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void loginPage_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void signupPage_ShouldReturnSignupViewWithDto() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attributeExists("signupRequest"));
    }

    @Test
    void signup_Successful_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("name", "Himanshu Kapse")
                        .param("email", "himanshu.kapse@example.com")
                        .param("password", "secret123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(userService, times(1)).signUp(any(SignupRequestDto.class));
    }


    @Test
    void signup_WithValidationErrors_ShouldReturnSignupView() throws Exception {
        // Sending empty params to trigger @Valid failures
        mockMvc.perform(post("/signup")
                        .param("username", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().hasErrors());

        verifyNoInteractions(userService);
    }

    @Test
    void signup_WhenServiceThrowsException_ShouldShowErrorMessage() throws Exception {
        // 1. Mock the service to throw the exception
        String expectedError = "Email already exists";
        doThrow(new IllegalArgumentException(expectedError))
                .when(userService).signUp(any(SignupRequestDto.class));

        mockMvc.perform(post("/signup")
                        .param("name", "Himanshu Kapse")
                        .param("email", "exists@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andExpect(model().attribute("errorMessage", expectedError));

        verify(userService).signUp(any(SignupRequestDto.class));
    }

}