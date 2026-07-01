package com.smartseats.demo.booking.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class WaitingQueueService {

    private final RedisTemplate<String, String> redisTemplate;

    // Key pattern: queue:{eventId}:{seatCategoryId}
    private static final String QUEUE_KEY = "queue:%d:%d";

    // Key pattern: queue:position:{eventId}:{seatCategoryId}:{userId}
    private static final String POSITION_KEY = "queue:position:%d:%d:%d";

    public WaitingQueueService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Add user to waiting queue
    // Returns their position in queue (1 = first in line)
    public long joinQueue(Long eventId, Long seatCategoryId, Long userId) {

        String queueKey = String.format(QUEUE_KEY, eventId, seatCategoryId);
        String positionKey = String.format(POSITION_KEY, eventId, seatCategoryId, userId);

        // Check if user is already in queue
        if (Boolean.TRUE.equals(redisTemplate.hasKey(positionKey))) {
            String existingPosition = redisTemplate.opsForValue().get(positionKey);
            return existingPosition != null ? Long.parseLong(existingPosition) : -1;
        }

        // Add user to end of queue (RPUSH = add to right/end)
        redisTemplate.opsForList().rightPush(queueKey, userId.toString());

        // Get their position
        Long queueSize = redisTemplate.opsForList().size(queueKey);
        long position = queueSize != null ? queueSize : 1;

        // Store their position with 30 min expiry
        redisTemplate.opsForValue().set(
                positionKey,
                String.valueOf(position),
                Duration.ofMinutes(30)
        );

        return position;
    }

    // Get user's current position in queue
    public long getPosition(Long eventId, Long seatCategoryId, Long userId) {
        String queueKey = String.format(QUEUE_KEY, eventId, seatCategoryId);
        String userIdStr = userId.toString();

        // Find actual position in list
        Long size = redisTemplate.opsForList().size(queueKey);
        if (size == null || size == 0) return -1;

        for (long i = 0; i < size; i++) {
            String member = redisTemplate.opsForList().index(queueKey, i);
            if (userIdStr.equals(member)) {
                return i + 1; // Position is 1-based
            }
        }
        return -1; // Not in queue
    }

    // Check if user is next in queue (position 1)
    public boolean isUserNext(Long eventId, Long seatCategoryId, Long userId) {
        String queueKey = String.format(QUEUE_KEY, eventId, seatCategoryId);
        String first = redisTemplate.opsForList().index(queueKey, 0);
        return userId.toString().equals(first);
    }

    // Remove user from front of queue after booking
    public void dequeue(Long eventId, Long seatCategoryId, Long userId) {
        String queueKey = String.format(QUEUE_KEY, eventId, seatCategoryId);
        String positionKey = String.format(POSITION_KEY,
                                           eventId, seatCategoryId, userId);

        // Remove from front of queue (LPOP = remove from left/front)
        redisTemplate.opsForList().leftPop(queueKey);

        // Remove position key
        redisTemplate.delete(positionKey);
    }

    // Remove user from queue (when they leave voluntarily)
    public void leaveQueue(Long eventId, Long seatCategoryId, Long userId) {
        String queueKey = String.format(QUEUE_KEY, eventId, seatCategoryId);
        String positionKey = String.format(POSITION_KEY,
                                           eventId, seatCategoryId, userId);

        redisTemplate.opsForList().remove(queueKey, 1, userId.toString());
        redisTemplate.delete(positionKey);
    }

    // Get total people waiting in queue
    public long getQueueSize(Long eventId, Long seatCategoryId) {
        String queueKey = String.format(QUEUE_KEY, eventId, seatCategoryId);
        Long size = redisTemplate.opsForList().size(queueKey);
        return size != null ? size : 0;
    }
}