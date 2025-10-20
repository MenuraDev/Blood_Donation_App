package com.blooddonation.app.service;

import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.model.EventRegister;
import com.blooddonation.app.repository.DonorRepository; // Assuming DonorRepository exists
import com.blooddonation.app.repository.EventRegisterRepository;
import com.blooddonation.app.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventRegisterService {

    @Autowired
    private EventRegisterRepository eventRegisterRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private DonorRepository donorRepository; // Assuming DonorRepository exists

    public EventRegister registerDonorForEvent(Long eventId, Long donorId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id " + donorId));

        // Check if donor is already registered for this event
        if (eventRegisterRepository.findByEventEventIdAndDonorUserId(eventId, donorId).isPresent()) {
            throw new IllegalArgumentException("Donor is already registered for this event.");
        }

        // Check if event capacity is reached
        Long registeredDonors = eventRegisterRepository.countByEventEventId(eventId);
        if (event.getMaxDonors() != null && registeredDonors >= event.getMaxDonors()) {
            throw new IllegalStateException("Event capacity reached. Cannot register more donors.");
        }

        EventRegister eventRegister = new EventRegister();
        eventRegister.setEvent(event);
        eventRegister.setDonor(donor);
        eventRegister.setRegistrationDate(LocalDate.now());

        return eventRegisterRepository.save(eventRegister);
    }

    public Long getRegisteredDonorsCountForEvent(Long eventId) {
        return eventRegisterRepository.countByEventEventId(eventId);
    }

    public List<Event> getEventsByRegistrationDate(LocalDate date) {
        return eventRegisterRepository.findByRegistrationDate(date).stream()
                .map(EventRegister::getEvent)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<EventRegister> getEventsRegisteredByDonor(Long donorId) {
        return eventRegisterRepository.findByDonorUserId(donorId);
    }

    public List<Donor> getDonorsRegisteredForEvent(Long eventId) {
        return eventRegisterRepository.findByEventEventId(eventId).stream()
                .map(EventRegister::getDonor)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<EventRegister> getEventRegisterById(Long id) {
        return eventRegisterRepository.findById(id);
    }

    public List<EventRegister> getAllEventRegisters() {
        return eventRegisterRepository.findAll();
    }

    public void deleteEventRegistration(Long id) {
        EventRegister eventRegister = eventRegisterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event registration not found with id " + id));
        eventRegisterRepository.delete(eventRegister);
    }
}
