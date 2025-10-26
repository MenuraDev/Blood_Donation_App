package com.blooddonation.app.repository;

import com.blooddonation.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventOrganizerUserId(Long eventOrganizerId);
    List<Event> findByEventNameContainingIgnoreCase(String eventName);
}
