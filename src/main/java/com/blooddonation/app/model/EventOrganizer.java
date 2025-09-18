package com.blooddonation.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("EVENT_ORGANIZER")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventOrganizer extends User {

    @Column(name = "organization_name")
    private String organizationName;

    @OneToMany(mappedBy = "eventOrganizer")
    private List<Event> events;
}
