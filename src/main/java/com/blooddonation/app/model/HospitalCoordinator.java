package com.blooddonation.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Keep NoArgsConstructor if needed

import java.time.LocalDate;

@Entity
@DiscriminatorValue("HOSPITAL_COORDINATOR")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class HospitalCoordinator extends User {
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    // Explicit constructor to handle the new 'role' field in User
    public HospitalCoordinator(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, Hospital hospital) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.hospital = hospital;
    }

    @Override
    public Role getRole() {
        return Role.HOSPITAL_COORDINATOR;
    }
}
