package com.blooddonation.app.controller;

import com.blooddonation.app.dto.BloodRequestRequest;
import com.blooddonation.app.model.BloodRequest;
import com.blooddonation.app.service.BloodRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-requests")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    @Autowired
    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @PreAuthorize("hasRole('HOSPITAL_COORDINATOR')")
    @PostMapping
    public ResponseEntity<BloodRequest> createBloodRequest(@RequestBody BloodRequestRequest bloodRequestRequest) {
        BloodRequest createdBloodRequest = bloodRequestService.createBloodRequest(bloodRequestRequest);
        return new ResponseEntity<>(createdBloodRequest, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('BLOOD_DONATION_MANAGER')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<BloodRequest> approveBloodRequest(@PathVariable Long id) {
        BloodRequest approvedBloodRequest = bloodRequestService.approveBloodRequest(id);
        return ResponseEntity.ok(approvedBloodRequest);
    }

    @PreAuthorize("hasRole('BLOOD_DONATION_MANAGER')")
    @PutMapping("/{id}/disapprove")
    public ResponseEntity<BloodRequest> disapproveBloodRequest(@PathVariable Long id) {
        BloodRequest disapprovedBloodRequest = bloodRequestService.disapproveBloodRequest(id);
        return ResponseEntity.ok(disapprovedBloodRequest);
    }

    // *** FIX: This is the endpoint for the BLOOD_DONATION_MANAGER to get ALL requests. ***
    // This removes the duplicate method error.
    @PreAuthorize("hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping
    public ResponseEntity<List<BloodRequest>> getAllBloodRequests() {
        List<BloodRequest> bloodRequests = bloodRequestService.getAllBloodRequests();
        return ResponseEntity.ok(bloodRequests);
    }

    // This endpoint is for a coordinator to get a SPECIFIC request by its ID.
    @PreAuthorize("hasRole('HOSPITAL_COORDINATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getBloodRequestById(@PathVariable Long id) {
        return bloodRequestService.getBloodRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // This endpoint is for a coordinator to UPDATE a request.
    @PreAuthorize("hasRole('HOSPITAL_COORDINATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<BloodRequest> updateBloodRequest(@PathVariable Long id, @RequestBody BloodRequestRequest bloodRequestDetails) {
        BloodRequest updatedBloodRequest = bloodRequestService.updateBloodRequest(id, bloodRequestDetails);
        return ResponseEntity.ok(updatedBloodRequest);
    }

    // This endpoint is for a coordinator to DELETE a request.
    @PreAuthorize("hasRole('HOSPITAL_COORDINATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodRequest(@PathVariable Long id) {
        bloodRequestService.deleteBloodRequest(id);
        return ResponseEntity.noContent().build();
    }

    // *** FIX: This is the endpoint for the HOSPITAL_COORDINATOR to get THEIR OWN requests. ***
    // This endpoint is distinct from the manager's endpoint above.
    @PreAuthorize("hasRole('HOSPITAL_COORDINATOR')")
    @GetMapping("/coordinator/{hospitalCoordinatorId}")
    public ResponseEntity<List<BloodRequest>> getBloodRequestsByHospitalCoordinatorId(@PathVariable Long hospitalCoordinatorId) {
        List<BloodRequest> bloodRequests = bloodRequestService.getBloodRequestsByHospitalCoordinatorId(hospitalCoordinatorId);
        return ResponseEntity.ok(bloodRequests);
    }
}
