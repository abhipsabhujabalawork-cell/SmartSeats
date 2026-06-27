package com.smartseats.demo.event.controller;

import com.smartseats.demo.event.dto.EventRequest;
import com.smartseats.demo.event.dto.EventResponse;
import com.smartseats.demo.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Create a new event
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request,
            @RequestHeader("X-User-Id") Long organizerId) {
        EventResponse response = eventService.createEvent(request, organizerId);
        return ResponseEntity.ok(response);
    }

    // Get single event
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // Get events by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<EventResponse>> getByCity(@PathVariable String city) {
        return ResponseEntity.ok(eventService.getEventsByCity(city));
    }

    // Publish an event
    @PutMapping("/{id}/publish")
    public ResponseEntity<EventResponse> publishEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.publishEvent(id));
    }
}