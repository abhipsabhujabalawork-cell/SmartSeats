package com.smartseats.demo.booking.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class SeatReservationService {

    private final RedisTemplate<String, String> redisTemplate;

    // How long a seat is held — 10 minutes
    private static final Duration RESERVATION_TTL = Duration.ofMinutes(10);

    // Key pattern: seat:hold:{eventId}:{seatCategoryId}:{userId}
    private static final String KEY_PATTERN = "seat:hold:%d:%d:%d";

    public SeatReservationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Hold seats for a user — returns false if already held by someone else
    public boolean holdSeats(Long eventId, Long seatCategoryId,
                             Long userId, Integer numberOfSeats) {

        String key = String.format(KEY_PATTERN, eventId, seatCategoryId, userId);

        // setIfAbsent = only set if key does NOT exist
        // This is atomic — no two users can hold same seat simultaneously
        Boolean held = redisTemplate.opsForValue()
                .setIfAbsent(key, numberOfSeats.toString(), RESERVATION_TTL);

        return Boolean.TRUE.equals(held);
    }

    // Release the hold — called when payment is done or user cancels
    public void releaseHold(Long eventId, Long seatCategoryId, Long userId) {
        String key = String.format(KEY_PATTERN, eventId, seatCategoryId, userId);
        redisTemplate.delete(key);
    }

    // Check if user already has a hold
    public boolean hasHold(Long eventId, Long seatCategoryId, Long userId) {
        String key = String.format(KEY_PATTERN, eventId, seatCategoryId, userId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // Get remaining time on hold in seconds
    public Long getHoldTimeRemaining(Long eventId, Long seatCategoryId, Long userId) {
        String key = String.format(KEY_PATTERN, eventId, seatCategoryId, userId);
        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return ttl != null ? ttl : 0L;
    }
}