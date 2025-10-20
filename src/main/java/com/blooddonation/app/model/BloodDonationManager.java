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
@DiscriminatorValue("BLOOD_DONATION_MANAGER")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class BloodDonationManager extends User{
    @OneToMany(mappedBy = "bloodDonationManager")
    private List<BloodRequest> approvedBloodRequests;

    // Explicit constructor to handle the new 'role' field in User
    public BloodDonationManager(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, List<BloodRequest> approvedBloodRequests) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.approvedBloodRequests = approvedBloodRequests;
    }

    @Override
    public Role getRole() {
        return Role.BLOOD_DONATION_MANAGER;

    }
}
