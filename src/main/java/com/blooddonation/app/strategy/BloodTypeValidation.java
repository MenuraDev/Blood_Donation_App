package com.blooddonation.app.strategy;

import com.blooddonation.app.model.BloodRequest;
import java.util.Arrays;
import java.util.List;

public class BloodTypeValidation implements BloodRequestValidationStrategy  {

    private static final List<String> VALID_BLOOD_TYPES = Arrays.asList(
        "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    );
    private String reason = "";

    @Override
    public boolean isValid(BloodRequest bloodRequest){
        if (bloodRequest.getBloodType() == null || bloodRequest.getBloodType().isEmpty())  {
            reason = "Blood type not provided.";
            return false;
        }
        if (!VALID_BLOOD_TYPES.contains(bloodRequest.getBloodType().toUpperCase())) {
            reason = "Invalid blood type: " + bloodRequest.getBloodType() + ". Valid types are: " + String.join(", ", VALID_BLOOD_TYPES);
            return false;
        }
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
