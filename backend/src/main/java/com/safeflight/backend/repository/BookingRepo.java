package com.safeflight.backend.repository;

import java.util.List;
import com.safeflight.backend.model.Booking;
import com.safeflight.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//Repository interface for managing Booking entities
public interface BookingRepo extends JpaRepository<Booking,Long> {
	List<Booking> findByUserOrderByBookingDateDesc(User user);
}
