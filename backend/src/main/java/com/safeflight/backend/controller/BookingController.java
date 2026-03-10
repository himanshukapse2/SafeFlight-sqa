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

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final FlightService flightService;
    private final FareService fareService;
    private final UserService userService;

    public BookingController(BookingService bookingService, FlightService flightService, FareService fareService,
            UserService userService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.fareService = fareService;
        this.userService = userService;
    }

    @GetMapping("/book/{flightId}")
    public String bookingForm(@PathVariable Long flightId, Model model) {
        Flight flight = flightService.getFlightById(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("bookingRequest", new BookingRequestDto());
        model.addAttribute("discountTypes", DiscountType.values());
        return "book";
    }

    @PostMapping("/book/{flightId}")
    public String createBooking(@PathVariable Long flightId,
            @Valid @ModelAttribute("bookingRequest") BookingRequestDto dto,
            BindingResult result,
            Authentication auth,
            Model model,
            RedirectAttributes redirectAttributes) {
        Flight flight = flightService.getFlightById(flightId);

        if (result.hasErrors()) {
            model.addAttribute("flight", flight);
            model.addAttribute("discountTypes", DiscountType.values());
            return "book";
        }

        User user = userService.findByEmail(auth.getName());

        try {
            Booking booking = bookingService.createBooking(user, flight, dto);

            // Build fare breakdown for confirmation
            FareBreakdownDto fare = fareService.calculateFare(flight, dto.getExtraBaggage(), dto.getDiscountType());
            redirectAttributes.addFlashAttribute("booking", booking);
            redirectAttributes.addFlashAttribute("fareBreakdown", fare);
            redirectAttributes.addFlashAttribute("successMessage", "Flight booked successfully!");

            return "redirect:/bookings/my";
        } catch (Exception e) {
            model.addAttribute("flight", flight);
            model.addAttribute("discountTypes", DiscountType.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "book";
        }
    }

    @GetMapping("/my")
    public String myBookings(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        List<Booking> upcoming = bookingService.getUpcomingBookings(user);
        List<Booking> past = bookingService.getPastBookings(user);
        model.addAttribute("upcomingBookings", upcoming);
        model.addAttribute("pastBookings", past);
        return "my-bookings";
    }

    @GetMapping("/cancel/{bookingId}")
    public String cancelConfirm(@PathVariable Long bookingId, Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        Booking booking = bookingService.getBookingById(bookingId);

        if (booking == null || booking.getUser() == null || user == null ||
        	    !java.util.Objects.equals(booking.getUser().getId(), user.getId())) {
        	    throw new IllegalArgumentException("You can only cancel your own bookings");
        	}

        String refundInfo = bookingService.getRefundInfo(booking);
        model.addAttribute("booking", booking);
        model.addAttribute("refundInfo", refundInfo);
        return "cancel-confirm";
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(auth.getName());
        Booking cancelled = bookingService.cancelBooking(bookingId, user);
        redirectAttributes.addFlashAttribute("successMessage",
                String.format("Booking #%d cancelled. Refund: ₹%.2f", cancelled.getId(),
                        cancelled.getRefundAmount() != null ? cancelled.getRefundAmount() : 0.0));
        return "redirect:/bookings/my";
    }
}
