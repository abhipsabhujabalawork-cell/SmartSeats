package com.smartseats.demo.booking.repository;

import com.smartseats.demo.booking.entity.Booking;
import com.smartseats.demo.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByEventId(Long eventId);

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
}