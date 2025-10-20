package com.blooddonation.app.strategy;

import com.blooddonation.app.model.BloodRequest;

public class PositiveQuantityValidation implements BloodRequestValidationStrategy {

    private String reason = "";

    @Override
    public boolean isValid(BloodRequest bloodRequest) {
        if (bloodRequest.getQuantity() == null || bloodRequest.getQuantity() <= 0) {
            reason = "Requested quantity must be a positive number.";
            return false;
        }
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
