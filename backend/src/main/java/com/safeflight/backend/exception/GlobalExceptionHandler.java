package com.safeflight.backend.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//GlobalExceptionHandler: Includes all the custom exceptions
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Not Found");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(SeatUnavailableException.class)
    public ModelAndView handleSeatUnavailable(SeatUnavailableException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Seats Unavailable");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Invalid Request");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneral(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorTitle", "Something Went Wrong");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again later.");
        return mav;
    }
}
