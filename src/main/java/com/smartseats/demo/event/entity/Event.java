package com.smartseats.demo.event.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String venue;

    private String city;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "booking_open_at")
    private LocalDateTime bookingOpenAt;

    @Column(name = "organizer_id", nullable = false)
    private Long organizerId;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeatCategory> seatCategories = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = EventStatus.DRAFT;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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
    public List<SeatCategory> getSeatCategories() { return seatCategories; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setCity(String city) { this.city = city; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(EventCategory category) { this.category = category; }
    public void setStatus(EventStatus status) { this.status = status; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
    public void setBookingOpenAt(LocalDateTime bookingOpenAt) { this.bookingOpenAt = bookingOpenAt; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
    public void setSeatCategories(List<SeatCategory> seatCategories) { this.seatCategories = seatCategories; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}