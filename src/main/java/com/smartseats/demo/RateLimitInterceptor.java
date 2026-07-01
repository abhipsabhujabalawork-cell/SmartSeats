package com.smartseats.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartseats.demo.notification.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;
    private final ObjectMapper objectMapper;

    public RateLimitInterceptor(RateLimiterService rateLimiterService,
                                ObjectMapper objectMapper) {
        this.rateLimiterService = rateLimiterService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String userIdHeader = request.getHeader("X-User-Id");

        // Skip rate limiting if no user id (public endpoints)
        if (userIdHeader == null) {
            return true;
        }

        Long userId = Long.parseLong(userIdHeader);
        String path = request.getRequestURI();

        // Stricter limit for booking endpoint
        if (path.contains("/api/bookings") &&
            request.getMethod().equals("POST")) {

            if (!rateLimiterService.isBookingAllowed(userId)) {
                sendRateLimitResponse(response,
                    "Too many booking attempts. Max 3 per minute allowed.");
                return false;
            }
        }

        // General rate limit for all other endpoints
        if (!rateLimiterService.isApiAllowed(userId)) {
            sendRateLimitResponse(response,
                "Too many requests. Max 10 per minute allowed.");
            return false;
        }

        return true;
    }

    private void sendRateLimitResponse(HttpServletResponse response,
                                        String message) throws Exception {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> error = new HashMap<>();
        error.put("status", 429);
        error.put("message", message);
        error.put("timestamp", java.time.LocalDateTime.now().toString());

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}