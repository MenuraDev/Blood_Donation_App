package com.blooddonation.app.dto;

import com.blooddonation.app.model.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;

    private String dob; // Assuming date of birth as String for simplicity in DTO

    private Integer age;

    @NotBlank
    @Size(min = 10, max = 20) // NIC can be 10 or 12 characters, so max 20 is safe
    private String nic;

    @NotBlank
    @Size(min = 10, max = 15)
    private String phone;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String gender;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Role role;
}
