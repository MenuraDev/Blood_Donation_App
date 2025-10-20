package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "blood_unit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnit  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unitId;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @Column(name = "collected_date") // Removed nullable = false
    private LocalDate collectedDate;

    @Column(name = "expire_date") // Removed nullable = false
    private LocalDate expireDate;

    @Column(name = "quantity")
    private Integer quantity; // Number of units

    @Column(name = "volume_ml")
    private Integer volumeMl; // Volume of the blood unit in ml

    @OneToOne
    @JoinColumn(name = "donation_id")
    private Donation donation; // Link to the original donation

    @OneToOne
    @JoinColumn(name = "blood_request_id") // Changed name for clarity
    private BloodRequest bloodRequest;
}
