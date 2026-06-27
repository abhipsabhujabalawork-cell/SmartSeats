package com.smartseats.demo.event.repository;

import com.smartseats.demo.event.entity.SeatCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatCategoryRepository extends JpaRepository<SeatCategory, Long> {

    @Query("SELECT s FROM SeatCategory s WHERE s.id = :id AND s.availableSeats >= :seats")
    java.util.Optional<SeatCategory> findByIdWithAvailableSeats(
        @Param("id") Long id,
        @Param("seats") Integer seats
    );
}