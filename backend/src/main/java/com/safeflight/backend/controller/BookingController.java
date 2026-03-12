package com.safeflight.backend.controller;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.safeflight.backend.dto.BookingRequestDto;
import com.safeflight.backend.dto.FareBreakdownDto;
import com.safeflight.backend.model.Booking;
import com.safeflight.backend.model.DiscountType;
import com.safeflight.backend.model.Flight;
import com.safeflight.backend.model.User;
import com.safeflight.backend.service.BookingService;
import com.safeflight.backend.service.FareService;
import com.safeflight.backend.service.FlightService;
import com.safeflight.backend.service.UserService;

import jakarta.validation.Valid;

// Handles all booking-related operations: creating bookings, viewing bookings, and cancellations
@Controller
@RequestMapping("/bookings")
public class BookingController {

    // Service for managing booking operations
    private final BookingService bookingService;
    // Service for retrieving flight information
    private final FlightService flightService;
    // Service for calculating fares and prices
    private final FareService fareService;
    // Service for user-related operations
    private final UserService userService;

    // Constructor to inject all required services
    public BookingController(BookingService bookingService, FlightService flightService, FareService fareService,
            UserService userService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.fareService = fareService;
        this.userService = userService;
    }

    // Display booking form with flight details and discount options
    @GetMapping("/book/{flightId}")
    public String bookingForm(@PathVariable Long flightId, Model model) {
        // Fetch flight details from database
        Flight flight = flightService.getFlightById(flightId);
        // Add flight info to model for display
        model.addAttribute("flight", flight);
        // Create empty booking form DTO
        model.addAttribute("bookingRequest", new BookingRequestDto());
        // Add discount type options to dropdown
        model.addAttribute("discountTypes", DiscountType.values());
        return "book";
    }

    // Process booking submission with validation and fare calculation
    @PostMapping("/book/{flightId}")
    public String createBooking(@PathVariable Long flightId,
            @Valid @ModelAttribute("bookingRequest") BookingRequestDto dto,
            BindingResult result,
            Authentication auth,
            Model model,
            RedirectAttributes redirectAttributes) {
        // Get flight details
        Flight flight = flightService.getFlightById(flightId);

        // Return to form if validation fails
        if (result.hasErrors()) {
            model.addAttribute("flight", flight);
            model.addAttribute("discountTypes", DiscountType.values());
            return "book";
        }

        // Get currently logged-in user
        User user = userService.findByEmail(auth.getName());

        try {
            // Save the booking to database
            Booking booking = bookingService.createBooking(user, flight, dto);

            // Calculate total fare
            FareBreakdownDto fare = fareService.calculateFare(flight, dto.getExtraBaggage(), dto.getDiscountType());
            // Pass booking confirmation details via redirect
            redirectAttributes.addFlashAttribute("booking", booking);
            redirectAttributes.addFlashAttribute("fareBreakdown", fare);
            redirectAttributes.addFlashAttribute("successMessage", "Flight booked successfully!");

            // Redirect to user's bookings list
            return "redirect:/bookings/my";
        } catch (Exception e) {
            // Show error message if booking fails
            model.addAttribute("flight", flight);
            model.addAttribute("discountTypes", DiscountType.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "book";
        }
    }

    // Display user's upcoming and past bookings
    @GetMapping("/my")
    public String myBookings(Authentication auth, Model model) {

        // Get logged-in user
        User user = userService.findByEmail(auth.getName());

        // Fetch future bookings (not yet departed)
        List<Booking> upcoming = bookingService.getUpcomingBookings(user);

        // Fetch completed and cancelled bookings
        List<Booking> past = bookingService.getPastBookings(user);

        // Add both lists to model
        model.addAttribute("upcomingBookings", upcoming);
        model.addAttribute("pastBookings", past);
        return "my-bookings";
    }

	// Display cancellation confirmation page with refund details
	@GetMapping("/cancel/{bookingId}")
	public String cancelConfirm(@PathVariable Long bookingId, Authentication auth, Model model) {
		// Get currently logged-in user
		User user = userService.findByEmail(auth.getName());

        // Retrieve booking details
		Booking booking = bookingService.getBookingById(bookingId);
		// Verify user owns this booking
		if (booking == null || booking.getUser() == null || user == null ||
				!java.util.Objects.equals(booking.getUser().getId(), user.getId())) {
			throw new IllegalArgumentException("You can only cancel your own bookings");
		}

		// Get refund amount based on cancellation policy
		String refundInfo = bookingService.getRefundInfo(booking);
		// Add details for confirmation page
		model.addAttribute("booking", booking);
		model.addAttribute("refundInfo", refundInfo);
		return "cancel-confirm";
	}

	// Process booking cancellation and update seat availability
	@PostMapping("/cancel/{bookingId}")
	public String cancelBooking(@PathVariable Long bookingId,
			Authentication auth,
			RedirectAttributes redirectAttributes) {
		// Get logged-in user
		User user = userService.findByEmail(auth.getName());

        // Cancel booking and get refund amount
		Booking cancelled = bookingService.cancelBooking(bookingId, user);

        // Show success message with refund details
		redirectAttributes.addFlashAttribute("successMessage",
				String.format("Booking #%d cancelled. Refund: €%.2f", cancelled.getId(),
						cancelled.getRefundAmount() != null ? cancelled.getRefundAmount() : 0.0));

        // Return to bookings list
		return "redirect:/bookings/my";
	}
}
