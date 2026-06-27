package com.smartseats.demo.booking.service;

import com.smartseats.demo.booking.dto.BookingRequest;
import com.smartseats.demo.booking.dto.BookingResponse;
import com.smartseats.demo.booking.entity.Booking;
import com.smartseats.demo.booking.entity.BookingStatus;
import com.smartseats.demo.booking.repository.BookingRepository;
import com.smartseats.demo.event.entity.Event;
import com.smartseats.demo.event.entity.EventStatus;
import com.smartseats.demo.event.entity.SeatCategory;
import com.smartseats.demo.event.repository.EventRepository;
import com.smartseats.demo.event.repository.SeatCategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final SeatCategoryRepository seatCategoryRepository;
    private final EntityManager entityManager;

    public BookingService(BookingRepository bookingRepository,
                          EventRepository eventRepository,
                          SeatCategoryRepository seatCategoryRepository,
                          EntityManager entityManager) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.seatCategoryRepository = seatCategoryRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long userId) {

        // Step 1 — Check event exists and is published
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new RuntimeException("Event is not available for booking");
        }

        // Step 2 — Lock the seat category row to prevent double booking
        // PESSIMISTIC_WRITE locks the DB row so no other transaction
        // can modify it until this transaction completes
        SeatCategory seatCategory = entityManager.find(
                SeatCategory.class,
                request.getSeatCategoryId(),
                LockModeType.PESSIMISTIC_WRITE
        );

        if (seatCategory == null) {
            throw new RuntimeException("Seat category not found");
        }

        // Step 3 — Check available seats
        if (seatCategory.getAvailableSeats() < request.getNumberOfSeats()) {
            throw new RuntimeException("Not enough seats available. Only "
                    + seatCategory.getAvailableSeats() + " seats left");
        }

        // Step 4 — Reduce available seats
        seatCategory.setAvailableSeats(
                seatCategory.getAvailableSeats() - request.getNumberOfSeats()
        );
        entityManager.merge(seatCategory);

        // Step 5 — Calculate total amount
        BigDecimal totalAmount = seatCategory.getPrice()
                .multiply(BigDecimal.valueOf(request.getNumberOfSeats()));

        // Step 6 — Create booking with unique reference
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setEventId(request.getEventId());
        booking.setSeatCategoryId(request.getSeatCategoryId());
        booking.setSeatCategoryName(seatCategory.getName());
        booking.setNumberOfSeats(request.getNumberOfSeats());
        booking.setPricePerSeat(seatCategory.getPrice());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookingReference("SS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        booking = bookingRepository.save(booking);
        return toResponse(booking);
    }

    public BookingResponse getBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to view this booking");
        }
        return toResponse(booking);
    }

    public List<BookingResponse> getMyBookings(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse cancelBooking(Long id, Long userId) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // Restore the seats back
        SeatCategory seatCategory = entityManager.find(
                SeatCategory.class,
                booking.getSeatCategoryId(),
                LockModeType.PESSIMISTIC_WRITE
        );

        if (seatCategory != null) {
            seatCategory.setAvailableSeats(
                    seatCategory.getAvailableSeats() + booking.getNumberOfSeats()
            );
            entityManager.merge(seatCategory);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);
        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getEventId(),
                booking.getSeatCategoryName(),
                booking.getNumberOfSeats(),
                booking.getPricePerSeat(),
                booking.getTotalAmount(),
                booking.getStatus(),
                booking.getBookingReference(),
                booking.getCreatedAt()
        );
    }
}