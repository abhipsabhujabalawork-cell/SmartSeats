package com.smartseats.demo.payment.dto;

import com.smartseats.demo.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    // Getters
    public Long getBookingId() { return bookingId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    // Setters
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}