package com.smartseats.demo.booking.controller;

import com.smartseats.demo.booking.dto.QueueResponse;
import com.smartseats.demo.booking.service.WaitingQueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final WaitingQueueService waitingQueueService;

    public QueueController(WaitingQueueService waitingQueueService) {
        this.waitingQueueService = waitingQueueService;
    }

    // Join the waiting queue
    @PostMapping("/join")
    public ResponseEntity<QueueResponse> joinQueue(
            @RequestParam Long eventId,
            @RequestParam Long seatCategoryId,
            @RequestHeader("X-User-Id") Long userId) {

        long position = waitingQueueService.joinQueue(
                eventId, seatCategoryId, userId);
        long totalInQueue = waitingQueueService.getQueueSize(
                eventId, seatCategoryId);
        boolean isYourTurn = waitingQueueService.isUserNext(
                eventId, seatCategoryId, userId);

        String message = isYourTurn
                ? "It's your turn! Proceed to book now."
                : "You are number " + position + " in queue. Please wait.";

        return ResponseEntity.ok(new QueueResponse(
                userId, eventId, seatCategoryId,
                position, totalInQueue, isYourTurn, message
        ));
    }

    // Check your position in queue
    @GetMapping("/position")
    public ResponseEntity<QueueResponse> getPosition(
            @RequestParam Long eventId,
            @RequestParam Long seatCategoryId,
            @RequestHeader("X-User-Id") Long userId) {

        long position = waitingQueueService.getPosition(
                eventId, seatCategoryId, userId);
        long totalInQueue = waitingQueueService.getQueueSize(
                eventId, seatCategoryId);
        boolean isYourTurn = waitingQueueService.isUserNext(
                eventId, seatCategoryId, userId);

        String message = position == -1
                ? "You are not in the queue."
                : isYourTurn
                    ? "It's your turn! Proceed to book now."
                    : "You are number " + position + " in queue.";

        return ResponseEntity.ok(new QueueResponse(
                userId, eventId, seatCategoryId,
                position, totalInQueue, isYourTurn, message
        ));
    }

    // Leave the queue voluntarily
    @DeleteMapping("/leave")
    public ResponseEntity<String> leaveQueue(
            @RequestParam Long eventId,
            @RequestParam Long seatCategoryId,
            @RequestHeader("X-User-Id") Long userId) {

        waitingQueueService.leaveQueue(eventId, seatCategoryId, userId);
        return ResponseEntity.ok("You have left the queue.");
    }
}