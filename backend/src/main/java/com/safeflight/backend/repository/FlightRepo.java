package com.safeflight.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.safeflight.backend.model.Flight;
import java.time.LocalDate;

//Repository interface for FlightRepo
public interface FlightRepo extends JpaRepository<Flight, Long>{
	List<Flight> findByDepartureDateAndFromCityAndToCity(LocalDate departureDate, String fromCity, String toCity);

}

