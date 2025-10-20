<<<<<<< Updated upstream
package com.blooddonation.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Keep NoArgsConstructor if needed

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("DONOR")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class Donor extends User {

    @Column(name = "address")
    private String address;

    @Column(name = "health_records")
    private String healthRecords; // Assuming health_records is a string for simplicity

    @Column(name = "blood_type")
    private String bloodType;

    @OneToMany(mappedBy = "donor")
    private List<Donation> donations;

    // Explicit constructor to handle the new 'role' field in User
    public Donor(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, String address, String healthRecords, String bloodType, List<Donation> donations) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.address = address;
        this.healthRecords = healthRecords;
        this.bloodType = bloodType;
        this.donations = donations;
    }

    @Override
    public Role getRole() {
        return Role.DONOR;
    }
}
=======
package com.blooddonation.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Keep NoArgsConstructor if needed

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@DiscriminatorValue("DONOR")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class Donor extends User {

    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EventRegister> eventRegisters;

    @Column(name = "address")
    private String address;

    @Column(name = "health_records")
    private String healthRecords; // Assuming health_records is a string for simplicity

    @Column(name = "blood_type")
    private String bloodType;

    @OneToMany(mappedBy = "donor")
    @JsonManagedReference
    private List<Donation> donations;

    // Explicit constructor to handle the new 'role' field in User
    public Donor(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, String address, String healthRecords, String bloodType, List<Donation> donations, List<EventRegister> eventRegisters) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.address = address;
        this.healthRecords = healthRecords;
        this.bloodType = bloodType;
        this.donations = donations;
        this.eventRegisters = eventRegisters;
    }

    @Override
    public Role getRole() {
        return Role.DONOR;
    }
}
>>>>>>> Stashed changes
