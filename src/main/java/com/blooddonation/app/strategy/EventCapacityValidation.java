package com.blooddonation.app.strategy;

import com.blooddonation.app.model.Event;

public class EventCapacityValidation implements EventValidationStrategy {

    private String reason = "";

    @Override
    public boolean isValid(Event event) {
        if (event.getMaxDonors() == null || event.getMaxDonors() <= 0) {
            reason = "Event capacity must be a positive number.";
            return false;
        }
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
