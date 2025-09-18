package com.blooddonation.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("DONOR")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Donor extends User {

    @Column(name = "address")
    private String address;

    @Column(name = "health_records")
    private String healthRecords; // Assuming health_records is a string for simplicity

    @Column(name = "blood_type")
    private String bloodType;

    @OneToMany(mappedBy = "donor")
    private List<Donation> donations;
}
