<<<<<<< Updated upstream
package com.blooddonation.app.service;

import com.blooddonation.app.dto.BloodRequestRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.BloodRequest;
import com.blooddonation.app.model.Hospital;
import com.blooddonation.app.model.HospitalCoordinator;
import com.blooddonation.app.repository.BloodRequestRepository;
import com.blooddonation.app.repository.HospitalRepository;
import com.blooddonation.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    public BloodRequest createBloodRequest(BloodRequestRequest bloodRequestRequest) {
        Hospital hospital = hospitalRepository.findById(bloodRequestRequest.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id " + bloodRequestRequest.getHospitalId()));

        HospitalCoordinator hospitalCoordinator = (HospitalCoordinator) userRepository.findById(bloodRequestRequest.getHospitalCoordinatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital Coordinator not found with id " + bloodRequestRequest.getHospitalCoordinatorId()));

        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setBloodType(bloodRequestRequest.getBloodType());
        bloodRequest.setReqStatus(bloodRequestRequest.getReqStatus());
        bloodRequest.setQuantity(bloodRequestRequest.getQuantity());
        bloodRequest.setReqDate(bloodRequestRequest.getReqDate());
        bloodRequest.setHospital(hospital);
        bloodRequest.setHospitalCoordinator(hospitalCoordinator);

        return bloodRequestRepository.save(bloodRequest);
    }

    public BloodRequest approveBloodRequest(Long requestId) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("BloodRequest not found with id " + requestId));
        bloodRequest.setReqStatus("Approved");
        return bloodRequestRepository.save(bloodRequest);
    }

    public BloodRequest disapproveBloodRequest(Long requestId) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("BloodRequest not found with id " + requestId));
        bloodRequest.setReqStatus("Disapproved");
        return bloodRequestRepository.save(bloodRequest);
    }

    public List<BloodRequest> getAllBloodRequests() {
        return bloodRequestRepository.findAll();
    }

    public Optional<BloodRequest> getBloodRequestById(Long id) {
        return bloodRequestRepository.findById(id);
    }

    public BloodRequest updateBloodRequest(Long id, BloodRequestRequest bloodRequestDetails) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodRequest not found with id " + id));

        Hospital hospital = hospitalRepository.findById(bloodRequestDetails.getHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id " + bloodRequestDetails.getHospitalId()));

        HospitalCoordinator hospitalCoordinator = (HospitalCoordinator) userRepository.findById(bloodRequestDetails.getHospitalCoordinatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Hospital Coordinator not found with id " + bloodRequestDetails.getHospitalCoordinatorId()));

        bloodRequest.setBloodType(bloodRequestDetails.getBloodType());
        bloodRequest.setReqStatus(bloodRequestDetails.getReqStatus());
        bloodRequest.setQuantity(bloodRequestDetails.getQuantity());
        bloodRequest.setReqDate(bloodRequestDetails.getReqDate());
        bloodRequest.setHospital(hospital);
        bloodRequest.setHospitalCoordinator(hospitalCoordinator);

        return bloodRequestRepository.save(bloodRequest);
    }

    public void deleteBloodRequest(Long id) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodRequest not found with id " + id));
        bloodRequestRepository.delete(bloodRequest);
    }

    public List<BloodRequest> getBloodRequestsByHospitalCoordinatorId(Long hospitalCoordinatorId) {
        return bloodRequestRepository.findByHospitalCoordinatorId(hospitalCoordinatorId);
    }
}
=======
package com.blooddonation.app.service;

import com.blooddonation.app.dto.BloodRequestRequest;
import com.blooddonation.app.model.BloodRequest;
import com.blooddonation.app.model.HospitalCoordinator;
import com.blooddonation.app.model.User;
import com.blooddonation.app.repository.BloodRequestRepository;
import com.blooddonation.app.repository.HospitalCoordinatorRepository;
import com.blooddonation.app.repository.UserRepository;
import com.blooddonation.app.strategy.BloodRequestValidationStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    @Autowired
    private HospitalCoordinatorRepository hospitalCoordinatorRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    private final StrategyConfigService strategyConfigService;

    @Autowired
    public BloodRequestService(BloodRequestRepository bloodRequestRepository, HospitalCoordinatorRepository hospitalCoordinatorRepository, UserRepository userRepository, StrategyConfigService strategyConfigService) {
        this.bloodRequestRepository = bloodRequestRepository;
        this.hospitalCoordinatorRepository = hospitalCoordinatorRepository;
        this.userRepository = userRepository; // Initialize UserRepository
        this.strategyConfigService = strategyConfigService;
    }

    public BloodRequest createBloodRequest(BloodRequestRequest requestDto) {
        BloodRequestValidationStrategy bloodRequestValidationStrategy = strategyConfigService.getActiveBloodRequestValidationStrategy();

        // Get the currently authenticated user's ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated.");
        }
        String username = authentication.getName(); // This is the phone number
        User authenticatedUser = userRepository.findByPhone(username)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found with phone: " + username));

        if (!(authenticatedUser instanceof HospitalCoordinator)) {
            throw new IllegalArgumentException("Authenticated user is not a Hospital Coordinator.");
        }
        HospitalCoordinator coordinator = (HospitalCoordinator) authenticatedUser;

        // Map DTO to the entity
        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setBloodType(requestDto.getBloodType());
        bloodRequest.setReqStatus(requestDto.getReqStatus() != null ? requestDto.getReqStatus() : "Pending"); // Default status
        bloodRequest.setQuantity(requestDto.getQuantity());
        bloodRequest.setReqDate(requestDto.getReqDate());
        bloodRequest.setHospitalName(requestDto.getHospitalName());
        bloodRequest.setHospitalCoordinator(coordinator); // Set the full entity object from authenticated user

        if (!bloodRequestValidationStrategy.isValid(bloodRequest)) {
            throw new IllegalArgumentException("Blood request validation failed: " + bloodRequestValidationStrategy.getReason());
        }

        return bloodRequestRepository.save(bloodRequest);
    }

    public BloodRequest approveBloodRequest(Long id) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BloodRequest not found with id: " + id));
        bloodRequest.setReqStatus("Approved");
        return bloodRequestRepository.save(bloodRequest);
    }

    public BloodRequest disapproveBloodRequest(Long id) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BloodRequest not found with id: " + id));
        bloodRequest.setReqStatus("Disapproved");
        return bloodRequestRepository.save(bloodRequest);
    }

    public List<BloodRequest> getAllBloodRequests() {
        return bloodRequestRepository.findAll();
    }

    public Optional<BloodRequest> getBloodRequestById(Long id) {
        return bloodRequestRepository.findById(id);
    }

    public BloodRequest updateBloodRequest(Long id, BloodRequestRequest requestDetails) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("BloodRequest not found with id: " + id));

        HospitalCoordinator coordinator = hospitalCoordinatorRepository.findById(requestDetails.getHospitalCoordinatorId())
                .orElseThrow(() -> new EntityNotFoundException("HospitalCoordinator not found with id: " + requestDetails.getHospitalCoordinatorId()));

        bloodRequest.setBloodType(requestDetails.getBloodType());
        bloodRequest.setReqStatus(requestDetails.getReqStatus());
        bloodRequest.setQuantity(requestDetails.getQuantity());
        bloodRequest.setReqDate(requestDetails.getReqDate());
        bloodRequest.setHospitalName(requestDetails.getHospitalName());
        bloodRequest.setHospitalCoordinator(coordinator);

        return bloodRequestRepository.save(bloodRequest);
    }

    public void deleteBloodRequest(Long id) {
        if (!bloodRequestRepository.existsById(id)) {
            throw new EntityNotFoundException("BloodRequest not found with id: " + id);
        }
        bloodRequestRepository.deleteById(id);
    }

    public List<BloodRequest> getBloodRequestsByHospitalCoordinatorId(Long hospitalCoordinatorId) {
        // Corrected to call the correct repository method name
        return bloodRequestRepository.findByHospitalCoordinatorUserId(hospitalCoordinatorId);
    }
}
>>>>>>> Stashed changes
