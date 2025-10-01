package com.blooddonation.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Import NoArgsConstructor

import java.time.LocalDate;

@Entity
@DiscriminatorValue("IT_OFFICER")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Add NoArgsConstructor
public class ITOfficer extends User {
    // No specific attributes for IT_officer in the ERD

    public ITOfficer(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
    }

    @Override
    public Role getRole() {
        return Role.IT_OFFICER;
    }
}
