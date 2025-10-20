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
@DiscriminatorValue("EVENT_ORGANIZER")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class EventOrganizer extends User {

    @Column(name = "organization_name")
    private String organizationName;

    @OneToMany(mappedBy = "eventOrganizer")
    private List<Event> events;

    // Explicit constructor to handle the new 'role' field in User
    public EventOrganizer(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, String organizationName, List<Event> events) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.organizationName = organizationName;
        this.events = events;
    }

    @Override
    public Role getRole() {
        return Role.EVENT_ORGANIZER;
    }
}
=======
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
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue("EVENT_ORGANIZER")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor // Keep NoArgsConstructor
public class EventOrganizer extends User {

    @Column(name = "organization_name")
    private String organizationName;

    @OneToMany(mappedBy = "eventOrganizer")
    @JsonManagedReference
    @JsonIgnore // Ignore events to break potential circular reference
    private List<Event> events;

    // Explicit constructor to handle the new 'role' field in User
    public EventOrganizer(Long userId, String firstName, String lastName, LocalDate dob, Integer age, String nic, String phone, String email, String gender, String password, String organizationName, List<Event> events) {
        super(userId, firstName, lastName, dob, age, nic, phone, email, gender, password);
        this.organizationName = organizationName;
        this.events = events;
    }

    @Override
    public Role getRole() {
        return Role.EVENT_ORGANIZER;
    }
}
>>>>>>> Stashed changes
