package com.blooddonation.app.repository;

import com.blooddonation.app.model.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByHospitalCoordinatorUserId(Long hospitalCoordinatorId);
}
