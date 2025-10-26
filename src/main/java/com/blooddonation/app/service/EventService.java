package com.blooddonation.app.service;

import com.blooddonation.app.dto.EventRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.model.EventOrganizer;
import com.blooddonation.app.repository.EventRepository;
import com.blooddonation.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.blooddonation.app.strategy.EventValidationStrategy;
import org.springframework.security.access.AccessDeniedException;
import com.blooddonation.app.config.CustomUserDetails; // Import CustomUserDetails
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private final StrategyConfigService strategyConfigService;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, StrategyConfigService strategyConfigService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.strategyConfigService = strategyConfigService;
    }

    public Event createEvent(EventRequest eventRequest) {
        EventValidationStrategy eventValidationStrategy = strategyConfigService.getActiveEventValidationStrategy();
        EventOrganizer eventOrganizer = (EventOrganizer) userRepository.findById(eventRequest.getEventOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Event Organizer not found with id " + eventRequest.getEventOrganizerId()));

        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setEventDate(eventRequest.getEventDate());
        event.setLocation(eventRequest.getLocation());
        event.setDescription(eventRequest.getDescription());
        event.setEventOrganizer(eventOrganizer);
        event.setMaxDonors(eventRequest.getMaxDonors()); // Assuming EventRequest has getMaxDonors()

        if (!eventValidationStrategy.isValid(event)) {
            throw new IllegalArgumentException("Event validation failed: " + eventValidationStrategy.getReason());
        }

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event updateEvent(Long id, EventRequest eventDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming the principal is an instance of your User model or a custom UserDetails that exposes the userId
        Long authenticatedUserId = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            authenticatedUserId = ((CustomUserDetails) principal).getUserId();
        } else {
            // Fallback if principal is not CustomUserDetails, e.g., a String (username/phone)
            authenticatedUserId = Long.parseLong(authentication.getName());
        }
        log.info("Authenticated User ID for update: {}", authenticatedUserId);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        log.info("Event Organizer ID for event {}: {}", id, event.getEventOrganizer().getUserId());

        if (!event.getEventOrganizer().getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not authorized to update this event.");
        }

        EventOrganizer eventOrganizer = (EventOrganizer) userRepository.findById(eventDetails.getEventOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Event Organizer not found with id " + eventDetails.getEventOrganizerId()));

        // Ensure the event organizer ID in the request matches the original organizer or the authenticated user
        if (!eventOrganizer.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You can only assign yourself as the organizer for this event.");
        }

        event.setEventName(eventDetails.getEventName());
        event.setEventDate(eventDetails.getEventDate());
        event.setLocation(eventDetails.getLocation());
        event.setDescription(eventDetails.getDescription());
        event.setEventOrganizer(eventOrganizer);

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming the principal is an instance of your User model or a custom UserDetails that exposes the userId
        Long authenticatedUserId = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            authenticatedUserId = ((CustomUserDetails) principal).getUserId();
        } else {
            // Fallback if principal is not CustomUserDetails, e.g., a String (username/phone)
            authenticatedUserId = Long.parseLong(authentication.getName());
        }
        log.info("Authenticated User ID for delete: {}", authenticatedUserId);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        log.info("Event Organizer ID for event {}: {}", id, event.getEventOrganizer().getUserId());

        // Get the roles of the authenticated user
        boolean isBloodDonationManager = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_BLOOD_DONATION_MANAGER"));

        // Allow BLOOD_DONATION_MANAGER to delete any event, otherwise restrict to event organizer
        if (!isBloodDonationManager && !event.getEventOrganizer().getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not authorized to delete this event.");
        }
        eventRepository.delete(event);
    }

    public List<Event> getEventsByEventOrganizerId(Long eventOrganizerId) {
        return eventRepository.findByEventOrganizerUserId(eventOrganizerId);
    }

    public List<Event> searchEventsByName(String eventName) {
        return eventRepository.findByEventNameContainingIgnoreCase(eventName);
    }
}
