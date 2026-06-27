package com.smartseats.demo.payment.controller;

import com.smartseats.demo.payment.dto.PaymentRequest;
import com.smartseats.demo.payment.dto.PaymentResponse;
import com.smartseats.demo.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Process a payment
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(paymentService.processPayment(request, userId));
    }

    // Get payment by booking
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBooking(
            @PathVariable Long bookingId,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentByBooking(bookingId, userId));
    }

    // Get all my payments
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(paymentService.getMyPayments(userId));
    }
}