package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "duration")
    private String duration; // Assuming duration is a string for simplicity (e.g., "2 hours", "full day")

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private EventOrganizer eventOrganizer;
}
