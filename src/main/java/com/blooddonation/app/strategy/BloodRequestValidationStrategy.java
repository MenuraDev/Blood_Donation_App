package com.blooddonation.app.strategy;

import com.blooddonation.app.model.BloodRequest;

public interface BloodRequestValidationStrategy {
    boolean isValid(BloodRequest bloodRequest);
    String getReason();
}
