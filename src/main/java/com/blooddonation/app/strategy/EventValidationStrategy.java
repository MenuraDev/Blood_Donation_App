package com.blooddonation.app.strategy;

import com.blooddonation.app.model.Event;

public interface EventValidationStrategy {
    boolean isValid(Event event);
    String getReason();
}
