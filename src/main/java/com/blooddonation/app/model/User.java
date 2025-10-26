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
    private String gender;

    @Column(name = "password", nullable = false)
    private String password;

    public enum Role {
        IT_OFFICER,
        BLOOD_DONATION_MANAGER,
        HOSPITAL_COORDINATOR,
        NURSE,
        DONOR,
        EVENT_ORGANIZER
    }

    // This method will be implemented by each concrete subclass to return its specific role.
    public abstract Role getRole();

    // The setter is removed as the role is immutable and defined by the entity's type.
}