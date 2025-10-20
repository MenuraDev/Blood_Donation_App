package com.blooddonation.app.service;

import com.blooddonation.app.model.Hospital;
import com.blooddonation.app.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public Optional<Hospital> findByName(String name) {
        return hospitalRepository.findByHospitalName(name);
    }
}
