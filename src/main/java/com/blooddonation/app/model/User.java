package com.blooddonation.app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "age")
    private Integer age;

    @Column(name = "nic", unique = true)
    private String nic;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "gender")
    private String gender; // Added gender field

    @Column(name = "password", nullable = false)
    private String password;

    // Removed the 'role' field as it's redundant with DiscriminatorColumn
    // The role will be derived from the user_type discriminator value.

    public enum Role {
        IT_OFFICER,
        BLOOD_DONATION_MANAGER,
        HOSPITAL_COORDINATOR,
        NURSE,
        DONOR,
        EVENT_ORGANIZER
    }

    // Method to get the role from the discriminator value (user_type)
    public Role getRole() {
        // This method should be overridden in subclasses to return their specific role
        // For the abstract User class, we can return null or throw an exception
        // or try to infer from the class name if no specific discriminator value is set.
        // However, with @DiscriminatorColumn, JPA handles this.
        // For now, we'll return null, and subclasses will provide the actual role.
        return null;
    }

    // Setter for role is removed as it's derived
    public void setRole(Role role) {
        // No-op or throw UnsupportedOperationException
    }
}
