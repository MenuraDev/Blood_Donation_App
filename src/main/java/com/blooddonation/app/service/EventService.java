package com.blooddonation.app.service;

import com.blooddonation.app.dto.EventRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.model.EventOrganizer;
import com.blooddonation.app.repository.EventRepository;
import com.blooddonation.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(EventRequest eventRequest) {
        EventOrganizer eventOrganizer = (EventOrganizer) userRepository.findById(eventRequest.getEventOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Event Organizer not found with id " + eventRequest.getEventOrganizerId()));

        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setEventDate(eventRequest.getEventDate());
        event.setLocation(eventRequest.getLocation());
        event.setDescription(eventRequest.getDescription());
        event.setEventOrganizer(eventOrganizer);

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event updateEvent(Long id, EventRequest eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        EventOrganizer eventOrganizer = (EventOrganizer) userRepository.findById(eventDetails.getEventOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Event Organizer not found with id " + eventDetails.getEventOrganizerId()));

        event.setEventName(eventDetails.getEventName());
        event.setEventDate(eventDetails.getEventDate());
        event.setLocation(eventDetails.getLocation());
        event.setDescription(eventDetails.getDescription());
        event.setEventOrganizer(eventOrganizer);

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
        eventRepository.delete(event);
    }

    public List<Event> getEventsByEventOrganizerId(Long eventOrganizerId) {
        return eventRepository.findByEventOrganizerUserId(eventOrganizerId);
    }
}
