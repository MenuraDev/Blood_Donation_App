package com.blooddonation.app.dto;

import com.blooddonation.app.model.Donation;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.model.Nurse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DonationResponse  {
    private Long id;
    private LocalDate donationDate;
    private String bloodType;
    private Integer quantityMl;
    private String bloodPressure;
    private Double hemoglobinLevel;
    private String status;
    private DonorDto donor; // Nested DTO for Donor
    private EventDto event; // Nested DTO for Event
    private NurseDto nurse; // Nested DTO for Nurse

    public DonationResponse(Donation donation)  {
        this.id = donation.getId();
        this.donationDate = donation.getDonationDate();
        this.bloodType = donation.getBloodType();
        this.quantityMl = donation.getQuantityMl();
        this.bloodPressure = donation.getBloodPressure();
        this.hemoglobinLevel = donation.getHemoglobinLevel();
        this.status = donation.getStatus();
        if (donation.getDonor() != null) {
            this.donor = new DonorDto(donation.getDonor());
        }
        if (donation.getEvent() != null) {
            this.event = new EventDto(donation.getEvent());
        }
        if (donation.getNurse() != null) {
            this.nurse = new NurseDto(donation.getNurse());
        }
    }

    @Data
    @NoArgsConstructor
    public static class DonorDto {
        private Long userId;
        private String firstName;
        private String lastName;

        public DonorDto(Donor donor) {
            this.userId = donor.getUserId();
            this.firstName = donor.getFirstName();
            this.lastName = donor.getLastName();
        }
    }

    @Data
    @NoArgsConstructor
    public static class EventDto {
        private Long eventId;
        private String eventName;

        public EventDto(Event event) {
            this.eventId = event.getEventId();
            this.eventName = event.getEventName();
        }
    }

    @Data
    @NoArgsConstructor
    public static class NurseDto {
        private Long userId;
        private String firstName;
        private String lastName;

        public NurseDto(Nurse nurse) {
            this.userId = nurse.getUserId();
            this.firstName = nurse.getFirstName();
            this.lastName = nurse.getLastName();
        }
    }
}
