package com.blooddonation.app.strategy;

import com.blooddonation.app.model.Donor;
import java.time.LocalDate;
import java.time.Period;

public class MinimumAgeEligibility implements DonationEligibilityStrategy {

    private static final int MIN_AGE = 18;
    private String reason = "";

    @Override
    public boolean isEligible(Donor donor) {
        if (donor.getDob() == null) {
            reason = "Date of birth not provided.";
            return false;
        }
        int age = Period.between(donor.getDob(), LocalDate.now()).getYears();
        if (age < MIN_AGE) {
            reason = "Donor is under the minimum age of " + MIN_AGE + " years.";
            return false;
        }
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
