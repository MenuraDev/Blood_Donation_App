package com.blooddonation.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("IT_OFFICER")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ITOfficer extends User {
    // No specific attributes for IT_officer in the ERD

    public ITOfficer() {
        super();
    }

    public ITOfficer(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String phone, String email, String password) {
        super(userId, firstName, lastName, dob, age, phone, email, password);
    }
}
