package com.smartseats.demo.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BookingRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Seat category ID is required")
    private Long seatCategoryId;

    @NotNull(message = "Number of seats is required")
    @Min(value = 1, message = "Must book at least 1 seat")
    private Integer numberOfSeats;

    // Getters
    public Long getEventId() { return eventId; }
    public Long getSeatCategoryId() { return seatCategoryId; }
    public Integer getNumberOfSeats() { return numberOfSeats; }

    // Setters
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public void setSeatCategoryId(Long seatCategoryId) { this.seatCategoryId = seatCategoryId; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }
}