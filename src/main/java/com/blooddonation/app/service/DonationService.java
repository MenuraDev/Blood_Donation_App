package com.blooddonation.app.service;

import com.blooddonation.app.dto.DonationRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.Donation;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.Nurse;
import com.blooddonation.app.repository.DonationRepository;
import com.blooddonation.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blooddonation.app.service.BloodUnitService; // Import BloodUnitService
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodUnitService bloodUnitService; // Autowire BloodUnitService

    public Donation createDonation(DonationRequest donationRequest) {
        Donor donor = (Donor) userRepository.findById(donationRequest.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id " + donationRequest.getDonorId()));

        Nurse nurse = (Nurse) userRepository.findById(donationRequest.getNurseId())
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id " + donationRequest.getNurseId()));

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setDonationDate(donationRequest.getDonationDate());
        donation.setBloodType(donationRequest.getBloodType());
        donation.setQuantityMl(donationRequest.getQuantityMl());
        donation.setStatus(donationRequest.getStatus());
        donation.setNurse(nurse);

        return donationRepository.save(donation);
    }

    public Donation approveDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + donationId));

        if (!"Approved".equals(donation.getStatus())) {
            donation.setStatus("Approved");
            Donation approvedDonation = donationRepository.save(donation);
            bloodUnitService.createBloodUnitFromDonation(approvedDonation); // Create blood unit upon approval
            return approvedDonation;
        }
        return donation; // Already approved
    }

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    public Donation updateDonation(Long id, DonationRequest donationDetails) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + id));

        Donor donor = (Donor) userRepository.findById(donationDetails.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id " + donationDetails.getDonorId()));

        Nurse nurse = (Nurse) userRepository.findById(donationDetails.getNurseId())
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id " + donationDetails.getNurseId()));

        donation.setDonor(donor);
        donation.setDonationDate(donationDetails.getDonationDate());
        donation.setBloodType(donationDetails.getBloodType());
        donation.setQuantityMl(donationDetails.getQuantityMl());
        donation.setStatus(donationDetails.getStatus());
        donation.setNurse(nurse);

        return donationRepository.save(donation);
    }

    public void deleteDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + id));
        donationRepository.delete(donation);
    }

    public List<Donation> getDonationsByNurseId(Long nurseId) {
        return donationRepository.findByNurseUserId(nurseId);
    }
}
