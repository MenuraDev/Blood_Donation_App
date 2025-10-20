package com.blooddonation.app.repository;

import com.blooddonation.app.model.BloodUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long>  {
}
