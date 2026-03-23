package com.safeflight.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RestController
class ExceptionTestController {
    @GetMapping("/test/not-found")
    public void throwNotFound() { throw new ResourceNotFoundException("Item not found"); }

    @GetMapping("/test/no-seats")
    public void throwNoSeats() throws SeatUnavailableException { throw new SeatUnavailableException("Full flight"); }

    @GetMapping("/test/illegal")
    public void throwIllegal() { throw new IllegalArgumentException("Bad input"); }

    @GetMapping("/test/general")
    public void throwGeneral() throws Exception { throw new Exception("Boom"); }
}

@WebMvcTest(controllers = ExceptionTestController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleNotFound_ShouldReturnErrorView() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorTitle", "Not Found"))
                .andExpect(model().attribute("errorMessage", "Item not found"));
    }

    @Test
    void handleSeatUnavailable_ShouldReturnErrorView() throws Exception {
        mockMvc.perform(get("/test/no-seats"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorTitle", "Seats Unavailable"))
                .andExpect(model().attribute("errorMessage", "Full flight"));
    }

    @Test
    void handleIllegalArgument_ShouldReturnErrorView() throws Exception {
        mockMvc.perform(get("/test/illegal"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorTitle", "Invalid Request"))
                .andExpect(model().attribute("errorMessage", "Bad input"));
    }

    @Test
    void handleGeneral_ShouldReturnErrorView() throws Exception {
        mockMvc.perform(get("/test/general"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorTitle", "Something Went Wrong"))
                .andExpect(model().attribute("errorMessage", "An unexpected error occurred. Please try again later."));
    }
}