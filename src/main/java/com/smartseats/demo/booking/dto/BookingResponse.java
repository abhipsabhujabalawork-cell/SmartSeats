package com.smartseats.demo.booking.dto;

import com.smartseats.demo.booking.entity.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long userId;
    private Long eventId;
    private String seatCategoryName;
    private Integer numberOfSeats;
    private BigDecimal pricePerSeat;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private String bookingReference;
    private LocalDateTime createdAt;

    public BookingResponse(Long id, Long userId, Long eventId,
                           String seatCategoryName, Integer numberOfSeats,
                           BigDecimal pricePerSeat, BigDecimal totalAmount,
                           BookingStatus status, String bookingReference,
                           LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.seatCategoryName = seatCategoryName;
        this.numberOfSeats = numberOfSeats;
        this.pricePerSeat = pricePerSeat;
        this.totalAmount = totalAmount;
        this.status = status;
        this.bookingReference = bookingReference;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getEventId() { return eventId; }
    public String getSeatCategoryName() { return seatCategoryName; }
    public Integer getNumberOfSeats() { return numberOfSeats; }
    public BigDecimal getPricePerSeat() { return pricePerSeat; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BookingStatus getStatus() { return status; }
    public String getBookingReference() { return bookingReference; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}