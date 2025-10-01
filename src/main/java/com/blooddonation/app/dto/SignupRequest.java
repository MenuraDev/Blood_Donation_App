package com.blooddonation.app.dto;

import com.blooddonation.app.model.User; // Import User to access Role enum
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequest {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Integer age;
    private String nic;
    private String phone;
    private String email;
    private String address;
    private String bloodType;
    private String gender;
    private String password;
    private String passwordConfirm;
    private User.Role role; // Added role field
    private String organizationName; // Added for EventOrganizer

    // Explicit getters to ensure they are available
    public String getEmail() {
        return email;
    }

    public String getNic() {
        return nic;
    }

    public String getAddress() {
        return address;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getOrganizationName() {
        return organizationName;
    }
}
