package com.blooddonation.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DonationRequest {
    private Long donorId;
    private LocalDate donationDate;
    private String bloodType;
    private Integer quantityMl;
    private String status; // e.g., "Pending", "Approved", "Rejected"
    private Long nurseId;
}
