<<<<<<< Updated upstream
package com.blooddonation.app.controller;

import com.blooddonation.app.dto.JwtResponse;
import com.blooddonation.app.dto.LoginRequest;
import com.blooddonation.app.dto.SignupRequest;
import com.blooddonation.app.dto.ErrorResponse;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.User;
import com.blooddonation.app.model.Nurse; // Explicitly import Nurse
import com.blooddonation.app.model.BloodDonationManager; // Explicitly import BloodDonationManager
import com.blooddonation.app.model.HospitalCoordinator; // Explicitly import HospitalCoordinator
import com.blooddonation.app.model.ITOfficer; // Explicitly import ITOfficer
import com.blooddonation.app.model.EventOrganizer; // Explicitly import EventOrganizer
import com.blooddonation.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.blooddonation.app.config.JwtUtil; // Import JwtUtil
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil; // Inject JwtUtil

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil; // Initialize JwtUtil
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(userDetails); // Generate JWT token

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority().replace("ROLE_", "")) // Strip "ROLE_" prefix
                .collect(Collectors.toList());

        User user = userService.findByPhone(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication."));

        return ResponseEntity.ok(new JwtResponse(jwt, user.getUserId(), user.getPhone(), user.getEmail(), roles)); // Use generated JWT
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.findByPhone(signupRequest.getPhone()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Phone number is already taken!"));
        }
        if (userService.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Email is already in use!"));
        }
        if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Passwords do not match!"));
        }

        User newUser;
        // Instantiate the correct subclass based on the role from signupRequest
        switch (signupRequest.getRole()) {
            case IT_OFFICER:
                newUser = new ITOfficer(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword());
                break;
            case BLOOD_DONATION_MANAGER:
                newUser = new BloodDonationManager(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), null); // Pass null for approvedBloodRequests initially
                break;
            case HOSPITAL_COORDINATOR:
                newUser = new HospitalCoordinator(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), null); // Pass null for hospital initially
                break;
            case NURSE:
                newUser = new Nurse(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), null); // Pass null for supervisedDonations initially
                break;
            case DONOR:
                newUser = new Donor(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), signupRequest.getAddress(), null, signupRequest.getBloodType(), null); // Pass null for healthRecords and donations initially
                break;
            case EVENT_ORGANIZER:
                newUser = new EventOrganizer(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), signupRequest.getOrganizationName(), null); // Pass null for events initially
                break;
            default:
                return ResponseEntity.badRequest().body(new ErrorResponse("Error: Invalid role specified!"));
        }

        newUser.setNic(signupRequest.getNic()); // Set NIC
        newUser.setPassword(signupRequest.getPassword()); // Password will be encoded in UserService

        // Handle specific fields for subclasses if necessary
        if (newUser instanceof Donor) {
            ((Donor) newUser).setAddress(signupRequest.getAddress());
            ((Donor) newUser).setBloodType(signupRequest.getBloodType());
        }
        // Add similar blocks for other specific roles if they have unique fields in SignupRequest

        userService.registerUser(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }
}
=======
package com.blooddonation.app.controller;

import com.blooddonation.app.dto.JwtResponse;
import com.blooddonation.app.dto.LoginRequest;
import com.blooddonation.app.dto.SignupRequest;
import com.blooddonation.app.dto.ErrorResponse;
import com.blooddonation.app.dto.MessageResponse;
import com.blooddonation.app.model.Donor;
import com.blooddonation.app.model.User;
import com.blooddonation.app.model.Nurse; // Explicitly import Nurse
import com.blooddonation.app.model.BloodDonationManager; // Explicitly import BloodDonationManager
import com.blooddonation.app.model.HospitalCoordinator; // Explicitly import HospitalCoordinator
import com.blooddonation.app.model.ITOfficer; // Explicitly import ITOfficer
import com.blooddonation.app.model.EventOrganizer; // Explicitly import EventOrganizer
import com.blooddonation.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.blooddonation.app.config.JwtUtil; // Import JwtUtil
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil; // Inject JwtUtil

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil; // Initialize JwtUtil
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getPhone(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(userDetails); // Generate JWT token

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        User user = userService.findByPhone(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after authentication."));

        return ResponseEntity.ok(new JwtResponse(jwt, user.getUserId(), user.getPhone(), user.getEmail(), roles)); // Use generated JWT
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.findByPhone(signupRequest.getPhone()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Phone number is already taken!"));
        }
        if (userService.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Email is already in use!"));
        }
        if (!signupRequest.getPassword().equals(signupRequest.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error: Passwords do not match!"));
        }

        User newUser;
        // Instantiate the correct subclass based on the role from signupRequest
        switch (signupRequest.getRole()) {
            case IT_OFFICER:
                newUser = new ITOfficer(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword());
                break;
            case BLOOD_DONATION_MANAGER:
                newUser = new BloodDonationManager(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), null); // Pass null for approvedBloodRequests initially
                break;
            case HOSPITAL_COORDINATOR:
                newUser = new HospitalCoordinator(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), null); // Pass null for hospital initially
                break;
            case NURSE:
                newUser = new Nurse(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), null); // Pass null for supervisedDonations initially
                break;
            case DONOR:
                newUser = new Donor(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), signupRequest.getAddress(), null, signupRequest.getBloodType(), null, null); // Pass null for healthRecords, donations, and eventRegisters initially
                break;
            case EVENT_ORGANIZER:
                newUser = new EventOrganizer(null, signupRequest.getFirstName(), signupRequest.getLastName(), signupRequest.getDob(), signupRequest.getAge(), signupRequest.getNic(), signupRequest.getPhone(), signupRequest.getEmail(), signupRequest.getGender(), signupRequest.getPassword(), signupRequest.getOrganizationName(), null); // Pass null for events initially
                break;
            default:
                return ResponseEntity.badRequest().body(new ErrorResponse("Error: Invalid role specified!"));
        }

        newUser.setNic(signupRequest.getNic()); // Set NIC
        newUser.setPassword(signupRequest.getPassword()); // Password will be encoded in UserService

        // Handle specific fields for subclasses if necessary
        if (newUser instanceof Donor) {
            ((Donor) newUser).setAddress(signupRequest.getAddress());
            ((Donor) newUser).setBloodType(signupRequest.getBloodType());
        }
        // Add similar blocks for other specific roles if they have unique fields in SignupRequest

        userService.registerUser(newUser);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
>>>>>>> Stashed changes
