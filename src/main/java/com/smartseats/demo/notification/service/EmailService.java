package com.smartseats.demo.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmation(String toEmail, String userName,
                                        String bookingReference, String eventName,
                                        Integer numberOfSeats, String totalAmount) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Booking Confirmed - " + bookingReference);

        String body = "Hi " + userName + ",\n\n" +
                "Your booking is confirmed!\n\n" +
                "Booking Reference: " + bookingReference + "\n" +
                "Event: " + eventName + "\n" +
                "Number of Seats: " + numberOfSeats + "\n" +
                "Total Amount: ₹" + totalAmount + "\n\n" +
                "Thank you for booking with SmartSeats!\n\n" +
                "Best regards,\n" +
                "SmartSeats Team";

        message.setText(body);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Don't fail the booking if email fails — just log it
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendCancellationEmail(String toEmail, String userName,
                                      String bookingReference) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Booking Cancelled - " + bookingReference);

        String body = "Hi " + userName + ",\n\n" +
                "Your booking " + bookingReference + " has been cancelled.\n" +
                "If applicable, your refund will be processed within 5-7 business days.\n\n" +
                "Best regards,\n" +
                "SmartSeats Team";

        message.setText(body);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}