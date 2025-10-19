package com.blooddonation.app.repository;

import com.blooddonation.app.model.HospitalCoordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalCoordinatorRepository extends JpaRepository<HospitalCoordinator, Long> {
}