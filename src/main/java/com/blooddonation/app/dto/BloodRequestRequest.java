package com.blooddonation.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BloodRequestRequest {
    private String bloodType;
    private String reqStatus;
    private Integer quantity;
    private LocalDate reqDate;
    private String hospitalName;
    private Long hospitalCoordinatorId;
}
