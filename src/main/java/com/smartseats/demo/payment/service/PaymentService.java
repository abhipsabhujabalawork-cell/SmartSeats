package com.smartseats.demo.payment.service;

import com.smartseats.demo.booking.entity.Booking;
import com.smartseats.demo.booking.entity.BookingStatus;
import com.smartseats.demo.booking.repository.BookingRepository;
import com.smartseats.demo.payment.dto.PaymentRequest;
import com.smartseats.demo.payment.dto.PaymentResponse;
import com.smartseats.demo.payment.entity.Payment;
import com.smartseats.demo.payment.entity.PaymentStatus;
import com.smartseats.demo.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request, Long userId) {

        // Step 1 — Find the booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Step 2 — Validate booking belongs to user
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to pay for this booking");
        }

        // Step 3 — Check booking status
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Cannot pay for a cancelled booking");
        }

        // Step 4 — Check if already paid
        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new RuntimeException("Payment already done for this booking");
        }

        // Step 5 — Simulate payment processing
        // In real world this would call Razorpay / Stripe API
        boolean paymentSuccess = simulatePaymentGateway();

        // Step 6 — Create payment record
        Payment payment = new Payment();
        payment.setBookingId(booking.getId());
        payment.setUserId(userId);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString()
                                              .substring(0, 8).toUpperCase());

        if (paymentSuccess) {
            payment.setStatus(PaymentStatus.SUCCESS);
            // Update booking status to confirmed
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        payment = paymentRepository.save(payment);
        return toResponse(payment);
    }

    public PaymentResponse getPaymentByBooking(Long bookingId, Long userId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to view this payment");
        }
        return toResponse(payment);
    }

    public List<PaymentResponse> getMyPayments(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Simulates a payment gateway — 90% success rate
    private boolean simulatePaymentGateway() {
        return Math.random() > 0.1;
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getBookingId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getCreatedAt()
        );
    }
}