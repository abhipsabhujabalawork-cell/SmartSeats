package com.smartseats.demo.booking.dto;

public class QueueResponse {

    private Long userId;
    private Long eventId;
    private Long seatCategoryId;
    private long position;
    private long totalInQueue;
    private boolean isYourTurn;
    private String message;

    public QueueResponse(Long userId, Long eventId, Long seatCategoryId,
                         long position, long totalInQueue,
                         boolean isYourTurn, String message) {
        this.userId = userId;
        this.eventId = eventId;
        this.seatCategoryId = seatCategoryId;
        this.position = position;
        this.totalInQueue = totalInQueue;
        this.isYourTurn = isYourTurn;
        this.message = message;
    }

    // Getters
    public Long getUserId() { return userId; }
    public Long getEventId() { return eventId; }
    public Long getSeatCategoryId() { return seatCategoryId; }
    public long getPosition() { return position; }
    public long getTotalInQueue() { return totalInQueue; }
    public boolean isYourTurn() { return isYourTurn; }
    public String getMessage() { return message; }
}