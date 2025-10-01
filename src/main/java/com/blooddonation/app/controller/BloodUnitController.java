package com.blooddonation.app.controller;

import com.blooddonation.app.model.BloodUnit;
import com.blooddonation.app.service.BloodUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-units")
public class BloodUnitController {

    @Autowired
    private BloodUnitService bloodUnitService;

    @PreAuthorize("hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping
    public ResponseEntity<List<BloodUnit>> getAllBloodUnits() {
        List<BloodUnit> bloodUnits = bloodUnitService.getAllBloodUnits();
        return ResponseEntity.ok(bloodUnits);
    }

    @PreAuthorize("hasRole('BLOOD_DONATION_MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<BloodUnit> getBloodUnitById(@PathVariable Long id) {
        return bloodUnitService.getBloodUnitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('BLOOD_DONATION_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodUnit(@PathVariable Long id) {
        bloodUnitService.deleteBloodUnit(id);
        return ResponseEntity.noContent().build();
    }
}
