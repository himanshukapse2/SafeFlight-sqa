package com.safeflight.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "flights")
public class Flight {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false) 
	private String flightNumber;
	
    @Column(nullable = false) 
    private String airline;
    
    @Column(nullable = false)
    private String fromCity;
    
    @Column(nullable = false) 
    private String toCity;
    
    @Column(nullable = false) 
    private LocalDate departureDate;
    
    @Column(nullable = false) 
    private LocalTime departureTime;
    
    @Column(nullable = false) 
    private LocalTime arrivalTime;
    
    @Column(nullable = false) 
    private Double basePrice;
    
    @Column(nullable = false) 
    private Integer availableSeats;

	@Column(nullable = false) 
    private Boolean domestic;
	
	public Flight(Long id, String flightNumber, String airline, String fromCity, String toCity,
            LocalDate departureDate, LocalTime departureTime, LocalTime arrivalTime,
            Double basePrice, Integer availableSeats, Boolean domestic) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        this.availableSeats = availableSeats;
        this.domestic = domestic;
    }

	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}

	public LocalTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Integer getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(Integer availableSeats) {
		this.availableSeats = availableSeats;
	}

	public Boolean getDomestic() {
		return domestic;
	}

	public void setDomestic(Boolean domestic) {
		this.domestic = domestic;
	}

}
