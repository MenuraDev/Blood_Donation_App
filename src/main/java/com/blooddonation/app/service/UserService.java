package com.blooddonation.app.service;

import com.blooddonation.app.dto.UserRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.User;
import com.blooddonation.app.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import com.blooddonation.app.model.*; // Import all models
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));

        // Use the overridden getRole() method from the concrete User subclass
        String role = user.getRole().name();
        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // CRUD operations for User Management
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User createUser(UserRequest userRequest) {
        User newUser;
        // Instantiate the correct subclass based on the role
        switch (userRequest.getRole()) {
            case IT_OFFICER:
                newUser = new ITOfficer(null, userRequest.getFirstName(), userRequest.getLastName(), LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(), userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(), userRequest.getPassword());
                break;
            case BLOOD_DONATION_MANAGER:
                newUser = new BloodDonationManager(null, userRequest.getFirstName(), userRequest.getLastName(), LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(), userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(), userRequest.getPassword(), null); // Pass null for approvedBloodRequests initially
                break;
            case HOSPITAL_COORDINATOR:
                newUser = new HospitalCoordinator(null, userRequest.getFirstName(), userRequest.getLastName(), LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(), userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(), userRequest.getPassword(), null); // Pass null for hospital initially
                break;
            case NURSE:
                newUser = new Nurse(null, userRequest.getFirstName(), userRequest.getLastName(), LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(), userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(), userRequest.getPassword(), null); // Pass null for supervisedDonations initially
                break;
            case DONOR:
                newUser = new Donor(null, userRequest.getFirstName(), userRequest.getLastName(), LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(), userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(), userRequest.getPassword(), null, null, null, null); // Pass null for address, healthRecords, bloodType, and donations initially
                break;
            case EVENT_ORGANIZER:
                newUser = new EventOrganizer(null, userRequest.getFirstName(), userRequest.getLastName(), LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(), userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(), userRequest.getPassword(), null, null); // Pass null for organizationName and events initially
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + userRequest.getRole());
        }

        newUser.setNic(userRequest.getNic());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userRepository.save(newUser);
    }

    public User updateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setFirstName(userRequest.getFirstName());
        existingUser.setLastName(userRequest.getLastName());
        existingUser.setDob(LocalDate.parse(userRequest.getDob()));
        existingUser.setAge(userRequest.getAge());
        existingUser.setNic(userRequest.getNic()); // Update NIC
        existingUser.setPhone(userRequest.getPhone());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setGender(userRequest.getGender());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
