package com.smartseats.demo.event.dto;

import com.smartseats.demo.event.entity.EventCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class EventRequest {

    @NotBlank(message = "Event name is required")
    private String name;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotBlank(message = "City is required")
    private String city;

    private String description;

    @NotNull(message = "Category is required")
    private EventCategory category;

    @NotNull(message = "Event date is required")
    private LocalDateTime eventDate;

    private LocalDateTime bookingOpenAt;

    @NotNull(message = "Seat categories are required")
    private List<SeatCategoryRequest> seatCategories;

    // Getters
    public String getName() { return name; }
    public String getVenue() { return venue; }
    public String getCity() { return city; }
    public String getDescription() { return description; }
    public EventCategory getCategory() { return category; }
    public LocalDateTime getEventDate() { return eventDate; }
    public LocalDateTime getBookingOpenAt() { return bookingOpenAt; }
    public List<SeatCategoryRequest> getSeatCategories() { return seatCategories; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setCity(String city) { this.city = city; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(EventCategory category) { this.category = category; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
    public void setBookingOpenAt(LocalDateTime bookingOpenAt) { this.bookingOpenAt = bookingOpenAt; }
    public void setSeatCategories(List<SeatCategoryRequest> seatCategories) { this.seatCategories = seatCategories; }
}