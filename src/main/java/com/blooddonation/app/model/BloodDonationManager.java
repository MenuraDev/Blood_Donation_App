package com.blooddonation.app.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("BLOOD_DONATION_MANAGER")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BloodDonationManager extends User {
    @OneToMany(mappedBy = "bloodDonationManager")
    private List<BloodUnit> managedBloodUnits;

    @OneToMany(mappedBy = "bloodDonationManager")
    private List<BloodRequest> approvedBloodRequests;
}
