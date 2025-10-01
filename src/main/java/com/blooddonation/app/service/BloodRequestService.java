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
