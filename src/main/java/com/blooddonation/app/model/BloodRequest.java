package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "blood_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @Column(name = "req_status")
    private String reqStatus; // e.g., "Pending", "Approved", "Rejected"

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "req_date")
    private LocalDate reqDate;

    @Column(name = "hospital_name")
    private String hospitalName;

    @ManyToOne
    @JoinColumn(name = "hospital_coordinator_id")
    private HospitalCoordinator hospitalCoordinator;

    @ManyToOne
    @JoinColumn(name = "blood_donation_manager_id")
    private BloodDonationManager bloodDonationManager;
}
