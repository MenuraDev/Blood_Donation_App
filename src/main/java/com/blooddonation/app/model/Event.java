<<<<<<< Updated upstream
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

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private EventOrganizer eventOrganizer;
}
=======
package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
// import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EventRegister> eventRegisters;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "max_donors")
    private Integer maxDonors;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private EventOrganizer eventOrganizer;
}
>>>>>>> Stashed changes
