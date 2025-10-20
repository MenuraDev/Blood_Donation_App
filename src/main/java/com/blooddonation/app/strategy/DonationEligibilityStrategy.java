package com.blooddonation.app.strategy;

import com.blooddonation.app.model.Donor;

public interface DonationEligibilityStrategy {
    boolean isEligible(Donor donor);
    String getReason();
}
