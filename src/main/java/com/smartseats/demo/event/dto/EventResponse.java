package com.smartseats.demo.event.dto;

import com.smartseats.demo.event.entity.EventCategory;
import com.smartseats.demo.event.entity.EventStatus;
import java.time.LocalDateTime;
import java.util.List;

public class EventResponse {

    private Long id;
    private String name;
    private String venue;
    private String city;
    private String description;
    private EventCategory category;
    private EventStatus status;
    private LocalDateTime eventDate;
    private LocalDateTime bookingOpenAt;
    private Long organizerId;
    private List<SeatCategoryResponse> seatCategories;
    private LocalDateTime createdAt;

    public EventResponse(Long id, String name, String venue, String city,
                         String description, EventCategory category,
                         EventStatus status, LocalDateTime eventDate,
                         LocalDateTime bookingOpenAt, Long organizerId,
                         List<SeatCategoryResponse> seatCategories,
                         LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.city = city;
        this.description = description;
        this.category = category;
        this.status = status;
        this.eventDate = eventDate;
        this.bookingOpenAt = bookingOpenAt;
        this.organizerId = organizerId;
        this.seatCategories = seatCategories;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getVenue() { return venue; }
    public String getCity() { return city; }
    public String getDescription() { return description; }
    public EventCategory getCategory() { return category; }
    public EventStatus getStatus() { return status; }
    public LocalDateTime getEventDate() { return eventDate; }
    public LocalDateTime getBookingOpenAt() { return bookingOpenAt; }
    public Long getOrganizerId() { return organizerId; }
    public List<SeatCategoryResponse> getSeatCategories() { return seatCategories; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}