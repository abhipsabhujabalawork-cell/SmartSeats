package com.smartseats.demo.event.repository;

import com.smartseats.demo.event.entity.Event;
import com.smartseats.demo.event.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(EventStatus status);

    List<Event> findByCity(String city);

    List<Event> findByOrganizerId(Long organizerId);
}