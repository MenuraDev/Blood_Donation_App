package com.blooddonation.app.controller;

import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.model.EventRegister;
import com.blooddonation.app.service.EventRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event-registers")
public class EventRegisterController {

    @Autowired
    private EventRegisterService eventRegisterService;

    @PostMapping("/register")
    public ResponseEntity<EventRegister> registerDonorForEvent(@RequestParam Long eventId, @RequestParam Long donorId) {
        try {
            EventRegister eventRegister = eventRegisterService.registerDonorForEvent(eventId, donorId);
            return new ResponseEntity<>(eventRegister, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/event/{eventId}/count")
    public ResponseEntity<Long> getRegisteredDonorsCountForEvent(@PathVariable Long eventId) {
        Long count = eventRegisterService.getRegisteredDonorsCountForEvent(eventId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Event>> getEventsByRegistrationDate(@PathVariable String date) {
        LocalDate registrationDate = LocalDate.parse(date);
        List<Event> events = eventRegisterService.getEventsByRegistrationDate(registrationDate);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<EventRegister>> getEventsRegisteredByDonor(@PathVariable Long donorId) {
        List<EventRegister> eventRegisters = eventRegisterService.getEventsRegisteredByDonor(donorId);
        return ResponseEntity.ok(eventRegisters);
    }

    @GetMapping("/event/{eventId}/donors")
    public ResponseEntity<List<Donor>> getDonorsRegisteredForEvent(@PathVariable Long eventId) {
        List<Donor> donors = eventRegisterService.getDonorsRegisteredForEvent(eventId);
        return ResponseEntity.ok(donors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRegister> getEventRegisterById(@PathVariable Long id) {
        Optional<EventRegister> eventRegister = eventRegisterService.getEventRegisterById(id);
        return eventRegister.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<EventRegister>> getAllEventRegisters() {
        List<EventRegister> eventRegisters = eventRegisterService.getAllEventRegisters();
        return ResponseEntity.ok(eventRegisters);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventRegistration(@PathVariable Long id) {
        try {
            eventRegisterService.deleteEventRegistration(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
