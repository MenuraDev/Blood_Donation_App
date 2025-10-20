<<<<<<< Updated upstream
package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "donation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    @Column(name = "donation_date")
    private LocalDate donationDate;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "quantity_ml")
    private Integer quantityMl;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Donor donor;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "nurse_id")
    private Nurse nurse;
}
=======
package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDate;

@Entity
@Table(name = "donation")
@Data
@NoArgsConstructor
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id") // Explicitly map to donation_id
    private Long id;

    @Column(name = "donation_date")
    private LocalDate donationDate;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "quantity_ml")
    private Integer quantityMl;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "hemoglobin_level")
    private Double hemoglobinLevel;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "donor_id")
    @JsonBackReference
    private Donor donor;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nurse_id")
    @JsonBackReference
    private Nurse nurse;

    public Donation(Long id, LocalDate donationDate, String bloodType, Integer quantityMl, 
                   String bloodPressure, Double hemoglobinLevel, String status, 
                   Donor donor, Event event, Nurse nurse) {
        this.id = id;
        this.donationDate = donationDate;
        this.bloodType = bloodType;
        this.quantityMl = quantityMl;
        this.bloodPressure = bloodPressure;
        this.hemoglobinLevel = hemoglobinLevel;
        this.status = status;
        this.donor = donor;
        this.event = event;
        this.nurse = nurse;
    }
}
>>>>>>> Stashed changes
