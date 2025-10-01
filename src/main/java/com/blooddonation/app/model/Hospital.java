package com.blooddonation.app.model;

import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "hospital")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalId;

    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "hospital")
    private List<HospitalCoordinator> hospitalCoordinators;

    @OneToMany(mappedBy = "hospital")
    private List<BloodRequest> bloodRequests;
}
