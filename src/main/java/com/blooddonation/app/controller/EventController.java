<<<<<<< Updated upstream
package com.blooddonation.app.controller;

import com.blooddonation.app.dto.EventRequest;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        Event createdEvent = eventService.createEvent(eventRequest);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventDetails) {
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    @GetMapping("/organizer/{eventOrganizerId}")
    public ResponseEntity<List<Event>> getEventsByEventOrganizerId(@PathVariable Long eventOrganizerId) {
        List<Event> events = eventService.getEventsByEventOrganizerId(eventOrganizerId);
        return ResponseEntity.ok(events);
    }
}
=======
package com.blooddonation.app.controller;

import com.blooddonation.app.dto.EventRequest;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('NURSE', 'BLOOD_DONATION_MANAGER', 'EVENT_ORGANIZER', 'IT_OFFICER')")
    public ResponseEntity<List<Event>> searchEvents(@RequestParam String eventName) {
        List<Event> events = eventService.searchEventsByName(eventName);
        return ResponseEntity.ok(events);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER')")
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventRequest eventRequest) {
        Event createdEvent = eventService.createEvent(eventRequest);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PreAuthorize("hasRole('DONOR') or hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEventsForView() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventDetails) {
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent);
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('EVENT_ORGANIZER') or hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping("/organizer/{eventOrganizerId}")
    public ResponseEntity<List<Event>> getEventsByEventOrganizerId(@PathVariable Long eventOrganizerId) {
        List<Event> events = eventService.getEventsByEventOrganizerId(eventOrganizerId);
        return ResponseEntity.ok(events);
    }
}
>>>>>>> Stashed changes
