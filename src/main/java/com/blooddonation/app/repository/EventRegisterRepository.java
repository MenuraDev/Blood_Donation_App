package com.blooddonation.app.repository;

import com.blooddonation.app.model.EventRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegisterRepository extends JpaRepository<EventRegister, Long> {
    Long countByEventEventId(Long eventId);
    List<EventRegister> findByRegistrationDate(LocalDate registrationDate);
    List<EventRegister> findByDonorUserId(Long donorId);
    List<EventRegister> findByEventEventId(Long eventId);
    Optional<EventRegister> findByEventEventIdAndDonorUserId(Long eventId, Long donorId);

    void deleteByDonor(com.blooddonation.app.model.Donor donor);
}
