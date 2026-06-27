package com.smartseats.demo.event.service;

import com.smartseats.demo.event.dto.*;
import com.smartseats.demo.event.entity.*;
import com.smartseats.demo.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public EventResponse createEvent(EventRequest request, Long organizerId) {

        // Create the event
        Event event = new Event();
        event.setName(request.getName());
        event.setVenue(request.getVenue());
        event.setCity(request.getCity());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setEventDate(request.getEventDate());
        event.setBookingOpenAt(request.getBookingOpenAt());
        event.setOrganizerId(organizerId);
        event.setStatus(EventStatus.DRAFT);

        // Create seat categories
        List<SeatCategory> categories = new ArrayList<>();
        for (SeatCategoryRequest catRequest : request.getSeatCategories()) {
            SeatCategory category = new SeatCategory();
            category.setName(catRequest.getName());
            category.setTotalSeats(catRequest.getTotalSeats());
            category.setAvailableSeats(catRequest.getTotalSeats());
            category.setPrice(catRequest.getPrice());
            category.setEvent(event);
            categories.add(category);
        }
        event.setSeatCategories(categories);

        event = eventRepository.save(event);
        return toResponse(event);
    }

    public EventResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return toResponse(event);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByCity(String city) {
        return eventRepository.findByCity(city)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponse publishEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatus.PUBLISHED);
        event = eventRepository.save(event);
        return toResponse(event);
    }

    // Convert Event entity to EventResponse DTO
    private EventResponse toResponse(Event event) {

        List<SeatCategoryResponse> catResponses = event.getSeatCategories()
                .stream()
                .map(cat -> new SeatCategoryResponse(
                        cat.getId(),
                        cat.getName(),
                        cat.getTotalSeats(),
                        cat.getAvailableSeats(),
                        cat.getPrice()
                ))
                .collect(Collectors.toList());

        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getVenue(),
                event.getCity(),
                event.getDescription(),
                event.getCategory(),
                event.getStatus(),
                event.getEventDate(),
                event.getBookingOpenAt(),
                event.getOrganizerId(),
                catResponses,
                event.getCreatedAt()
        );
    }
}