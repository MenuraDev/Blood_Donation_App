package com.blooddonation.app.strategy;

import com.blooddonation.app.model.Event;
import java.time.LocalDate;

public class FutureEventDateValidation implements EventValidationStrategy {

    private String reason = "";

    @Override
    public boolean isValid(Event event) {
        if (event.getEventDate() == null) {
            reason = "Event date not provided.";
            return false;
        }
        if (event.getEventDate().isBefore(LocalDate.now())) {
            reason = "Event date cannot be in the past.";
            return false;
        }
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
