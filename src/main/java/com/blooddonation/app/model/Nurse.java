package com.blooddonation.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Keep NoArgsConstructor if needed

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("NURSE")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class Nurse extends User {
    @OneToMany(mappedBy = "nurse")
    private List<Donation> supervisedDonations;

    // Explicit constructor to handle the new 'role' field in User
    public Nurse(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, List<Donation> supervisedDonations) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.supervisedDonations = supervisedDonations;
    }

    @Override
    public Role getRole() {
        return Role.NURSE;
    }
}
