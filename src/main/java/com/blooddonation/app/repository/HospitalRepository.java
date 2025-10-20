<<<<<<< Updated upstream
package com.blooddonation.app.repository;

import com.blooddonation.app.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
=======
package com.blooddonation.app.repository;

import com.blooddonation.app.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findByHospitalName(String hospitalName);
}
>>>>>>> Stashed changes
