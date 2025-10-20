package com.blooddonation.app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DonationRequest {
    private Long donorId;
    private LocalDate donationDate;
    private String bloodType;
    private Integer quantityMl;
    private String bloodPressure;
    private Double hemoglobinLevel;
    private String status;
    private Long nurseId;
    private Long eventId;
}