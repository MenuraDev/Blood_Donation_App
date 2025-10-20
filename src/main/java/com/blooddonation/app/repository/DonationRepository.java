<<<<<<< Updated upstream
package com.blooddonation.app.repository;

import com.blooddonation.app.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByNurseId(Long nurseId);
}
=======
package com.blooddonation.app.repository;

import com.blooddonation.app.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT d FROM Donation d LEFT JOIN FETCH d.donor LEFT JOIN FETCH d.nurse LEFT JOIN FETCH d.event")
    List<Donation> findAll();

    @Query("SELECT d FROM Donation d LEFT JOIN FETCH d.donor LEFT JOIN FETCH d.nurse LEFT JOIN FETCH d.event WHERE d.id = :id")
    Optional<Donation> findById(Long id);

    @Query("SELECT d FROM Donation d LEFT JOIN FETCH d.donor LEFT JOIN FETCH d.nurse LEFT JOIN FETCH d.event WHERE d.nurse.userId = :nurseId")
    List<Donation> findByNurseUserId(Long nurseId);

    @Query("SELECT d FROM Donation d LEFT JOIN FETCH d.donor LEFT JOIN FETCH d.nurse LEFT JOIN FETCH d.event WHERE d.donor.userId = :donorId")
    List<Donation> findByDonorUserId(Long donorId);

    void deleteByDonor(com.blooddonation.app.model.Donor donor);
}
>>>>>>> Stashed changes
