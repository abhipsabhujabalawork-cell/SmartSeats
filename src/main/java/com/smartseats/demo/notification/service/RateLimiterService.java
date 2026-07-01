package com.smartseats.demo.notification.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, String> redisTemplate;

    // Max requests allowed per window
    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final int MAX_BOOKING_REQUESTS_PER_MINUTE = 3;

    public RateLimiterService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Returns true if request is allowed, false if rate limit exceeded
    public boolean isAllowed(String key, int maxRequests) {

        String redisKey = "rate:limit:" + key;

        // Increment the counter
        Long count = redisTemplate.opsForValue().increment(redisKey);

        // First request — set expiry of 1 minute
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, Duration.ofMinutes(1));
        }

        return count != null && count <= maxRequests;
    }

    // General API rate limit — 10 requests per minute
    public boolean isApiAllowed(Long userId) {
        return isAllowed("api:" + userId, MAX_REQUESTS_PER_MINUTE);
    }

    // Booking rate limit — max 3 booking attempts per minute
    public boolean isBookingAllowed(Long userId) {
        return isAllowed("booking:" + userId, MAX_BOOKING_REQUESTS_PER_MINUTE);
    }
}