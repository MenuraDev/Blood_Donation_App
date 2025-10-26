package com.blooddonation.app.strategy;

import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.Donation; // Assuming Donation model exists
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Optional;

public class RecentDonationEligibility implements DonationEligibilityStrategy {

    private static final long DAYS_BETWEEN_DONATIONS = 56; // 8 weeks
    private String reason = "";

    @Override
    public boolean isEligible(Donor donor) {
        // Assuming Donor has a method to get their last donation date or a list of donations
        // For simplicity, let's assume we can get the last donation date directly or find it from a list
        // If the Donor object doesn't contain this, we would need to inject a service/repository
        // to fetch donation history.

        // Placeholder: In a real application, you'd fetch the last donation date from the database
        // For this example, let's assume Donor has a method getLastDonationDate()
        // Or, if Donor has a list of donations, find the latest one.
        
        // For now, let's assume the Donor object has a list of donations and we find the latest one.
        // If the Donor model doesn't have this, we'll need to modify it or fetch from repository.
        // Ensure donations list is not null before streaming
        Optional<Donation> lastDonation = donor.getDonations() != null ?
                                            donor.getDonations().stream()
                                            .max(Comparator.comparing(Donation::getDonationDate)) :
                                            Optional.empty();

        if (lastDonation.isPresent()) {
            LocalDate lastDonationDate = lastDonation.get().getDonationDate();
            long daysSinceLastDonation = ChronoUnit.DAYS.between(lastDonationDate, LocalDate.now());
            if (daysSinceLastDonation < DAYS_BETWEEN_DONATIONS) {
                reason = "Donor donated recently. Must wait " + (DAYS_BETWEEN_DONATIONS - daysSinceLastDonation) + " more days.";
                return false;
            }
        }
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
