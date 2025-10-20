package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "event_register")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
}
