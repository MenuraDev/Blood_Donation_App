<<<<<<< Updated upstream
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
        return donationRepository.findByNurseId(nurseId);
    }
}
=======
package com.blooddonation.app.service;

import com.blooddonation.app.dto.DonationRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.Donation;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.Event;
import com.blooddonation.app.model.User; // Add this import
import com.blooddonation.app.model.Nurse;
import com.blooddonation.app.repository.DonationRepository;
import com.blooddonation.app.repository.EventRepository;
import com.blooddonation.app.repository.UserRepository;
import com.blooddonation.app.strategy.DonationEligibilityStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DonationService {

    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository; // Autowire EventRepository

    @Autowired
    private BloodUnitService bloodUnitService; // Autowire BloodUnitService

    private final StrategyConfigService strategyConfigService;

    @Autowired
    public DonationService(DonationRepository donationRepository, UserRepository userRepository, EventRepository eventRepository, BloodUnitService bloodUnitService, StrategyConfigService strategyConfigService) {
        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.bloodUnitService = bloodUnitService;
        this.strategyConfigService = strategyConfigService;
    }

    public Donation createDonation(DonationRequest donationRequest) {
    DonationEligibilityStrategy donationEligibilityStrategy = strategyConfigService.getActiveDonationEligibilityStrategy();
    Donor donor = (Donor) userRepository.findById(donationRequest.getDonorId())
            .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id " + donationRequest.getDonorId()));

    if (!donationEligibilityStrategy.isEligible(donor)) {
        throw new IllegalArgumentException("Donor is not eligible for donation: " + donationEligibilityStrategy.getReason());
    }

    Nurse nurse = (Nurse) userRepository.findById(donationRequest.getNurseId())
            .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id " + donationRequest.getNurseId()));

    Event event = eventRepository.findById(donationRequest.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + donationRequest.getEventId()));

    Donation donation = new Donation();
    donation.setDonor(donor);
    donation.setDonationDate(donationRequest.getDonationDate());
    donation.setBloodType(donationRequest.getBloodType()); // Set from request
    donation.setQuantityMl(donationRequest.getQuantityMl());
    donation.setBloodPressure(donationRequest.getBloodPressure());
    donation.setHemoglobinLevel(donationRequest.getHemoglobinLevel());
    donation.setStatus("PENDING"); // Default status
    donation.setNurse(nurse);
    donation.setEvent(event);

    Donation savedDonation = donationRepository.save(donation);
    // Explicitly initialize donor and nurse to ensure they are fetched
    savedDonation.getDonor().getFirstName(); 
    savedDonation.getNurse().getFirstName();
    logger.info("Created Donation: {}", savedDonation);
    return savedDonation;
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

    public Donation disapproveDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + donationId));

        if (!"Disapproved".equals(donation.getStatus())) {
            donation.setStatus("Disapproved");
            return donationRepository.save(donation);
        }
        return donation; // Already disapproved
    }

    public List<Donation> getAllDonations() {
        List<Donation> donations = donationRepository.findAll(); // This will now use the custom query
        // Explicitly initialize donor and nurse for each donation
        donations.forEach(donation -> {
            if (donation.getDonor() != null) donation.getDonor().getFirstName();
            if (donation.getNurse() != null) donation.getNurse().getFirstName();
        });
        return donations;
    }

    public Optional<Donation> getDonationById(Long id) {
        Optional<Donation> donation = donationRepository.findById(id); // This will now use the custom query
        // Explicitly initialize donor and nurse if present
        donation.ifPresent(d -> {
            if (d.getDonor() != null) d.getDonor().getFirstName();
            if (d.getNurse() != null) d.getNurse().getFirstName();
        });
        logger.info("Retrieved Donation by ID: {}", donation);
        return donation;
    }

    public Donation updateDonation(Long id, DonationRequest donationDetails) {
    Donation donation = donationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + id));

    Donor donor = (Donor) userRepository.findById(donationDetails.getDonorId())
            .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id " + donationDetails.getDonorId()));

    Nurse nurse = (Nurse) userRepository.findById(donationDetails.getNurseId())
            .orElseThrow(() -> new ResourceNotFoundException("Nurse not found with id " + donationDetails.getNurseId()));

    Event event = eventRepository.findById(donationDetails.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + donationDetails.getEventId()));

    donation.setDonor(donor);
    donation.setDonationDate(donationDetails.getDonationDate());
    donation.setBloodType(donationDetails.getBloodType()); // Set from request
    donation.setQuantityMl(donationDetails.getQuantityMl());
    donation.setBloodPressure(donationDetails.getBloodPressure());
    donation.setHemoglobinLevel(donationDetails.getHemoglobinLevel());
    donation.setNurse(nurse);
    donation.setEvent(event);

    Donation updatedDonation = donationRepository.save(donation);
    // Explicitly initialize donor and nurse to ensure they are fetched
    updatedDonation.getDonor().getFirstName();
    updatedDonation.getNurse().getFirstName();
    logger.info("Updated Donation: {}", updatedDonation);
    return updatedDonation;
}

    public void deleteDonation(Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found with id " + id));
        donationRepository.delete(donation);
    }

    public List<Donation> getDonationsByNurseId(Long nurseId) {
        return donationRepository.findByNurseUserId(nurseId);
    }

    public List<Donation> getDonationsByDonorId(Long donorId) {
        List<Donation> donations = donationRepository.findByDonorUserId(donorId);
        donations.forEach(donation -> {
            if (donation.getDonor() != null) donation.getDonor().getFirstName();
            if (donation.getNurse() != null) donation.getNurse().getFirstName();
            if (donation.getEvent() != null) donation.getEvent().getEventName();
        });
        return donations;
    }
}
>>>>>>> Stashed changes
