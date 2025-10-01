package com.blooddonation.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequest {
    private String eventName;
    private LocalDate eventDate;
    private String location;
    private String description;
    private Long eventOrganizerId;
}
