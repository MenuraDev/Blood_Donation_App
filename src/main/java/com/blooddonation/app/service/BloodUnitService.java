package com.blooddonation.app.service;

import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.BloodUnit;
import com.blooddonation.app.model.Donation;
import com.blooddonation.app.repository.BloodUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BloodUnitService {

    @Autowired
    private BloodUnitRepository bloodUnitRepository;

    public BloodUnit createBloodUnitFromDonation(Donation donation) {
        BloodUnit bloodUnit = new BloodUnit();
        bloodUnit.setBloodType(donation.getBloodType());
        bloodUnit.setCollectedDate(donation.getDonationDate());
        bloodUnit.setQuantity(1); // Assuming one donation creates one blood unit
        bloodUnit.setVolumeMl(donation.getQuantityMl()); // Store the volume from the donation
        bloodUnit.setExpireDate(donation.getDonationDate().plusDays(50)); // 50 days shelf life from collected date
        bloodUnit.setDonation(donation); // Link to the original donation
        return bloodUnitRepository.save(bloodUnit);
    }

    public List<BloodUnit> getAllBloodUnits() {
        return bloodUnitRepository.findAll();
    }

    public Optional<BloodUnit> getBloodUnitById(Long id) {
        return bloodUnitRepository.findById(id);
    }

    public BloodUnit updateBloodUnit(Long id, BloodUnit bloodUnitDetails) {
        BloodUnit bloodUnit = bloodUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodUnit not found with id " + id));

        bloodUnit.setBloodType(bloodUnitDetails.getBloodType());
        bloodUnit.setQuantity(bloodUnitDetails.getQuantity());
        bloodUnit.setVolumeMl(bloodUnitDetails.getVolumeMl());

        // Update collected date and recalculate expire date if collected date is provided
        if (bloodUnitDetails.getCollectedDate() != null) {
            bloodUnit.setCollectedDate(bloodUnitDetails.getCollectedDate());
            bloodUnit.setExpireDate(bloodUnitDetails.getCollectedDate().plusDays(50));
        } else {
            // If collected date is not provided, ensure expire date is still calculated based on existing collected date
            bloodUnit.setExpireDate(bloodUnit.getCollectedDate().plusDays(50));
        }
        // Assuming donation and bloodRequest are not updated via this method directly
        return bloodUnitRepository.save(bloodUnit);
    }

    public void deleteBloodUnit(Long id) {
        BloodUnit bloodUnit = bloodUnitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodUnit not found with id " + id));
        bloodUnitRepository.delete(bloodUnit);
    }
}
