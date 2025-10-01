package com.blooddonation.app.controller;

import com.blooddonation.app.dto.DonationRequest;
import com.blooddonation.app.model.Donation;
import com.blooddonation.app.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @PreAuthorize("hasRole('NURSE')")
    @PostMapping
    public ResponseEntity<Donation> createDonation(@RequestBody DonationRequest donationRequest) {
        Donation createdDonation = donationService.createDonation(donationRequest);
        return new ResponseEntity<>(createdDonation, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('NURSE')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Donation> approveDonation(@PathVariable Long id) {
        Donation approvedDonation = donationService.approveDonation(id);
        return ResponseEntity.ok(approvedDonation);
    }

    @PreAuthorize("hasRole('NURSE')")
    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {
        List<Donation> donations = donationService.getAllDonations();
        return ResponseEntity.ok(donations);
    }

    @PreAuthorize("hasRole('NURSE')")
    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        return donationService.getDonationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('NURSE')")
    @PutMapping("/{id}")
    public ResponseEntity<Donation> updateDonation(@PathVariable Long id, @RequestBody DonationRequest donationDetails) {
        Donation updatedDonation = donationService.updateDonation(id, donationDetails);
        return ResponseEntity.ok(updatedDonation);
    }

    @PreAuthorize("hasRole('NURSE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        donationService.deleteDonation(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('NURSE')")
    @GetMapping("/nurse/{nurseId}")
    public ResponseEntity<List<Donation>> getDonationsByNurseId(@PathVariable Long nurseId) {
        List<Donation> donations = donationService.getDonationsByNurseId(nurseId);
        return ResponseEntity.ok(donations);
    }
}
