package com.blooddonation.app.service;

import com.blooddonation.app.dto.UserRequest;
import com.blooddonation.app.exception.ResourceNotFoundException;
import com.blooddonation.app.model.User;
import com.blooddonation.app.repository.UserRepository;
import com.blooddonation.app.repository.DonorRepository;
import com.blooddonation.app.repository.DonationRepository; // Import DonationRepository
import com.blooddonation.app.repository.EventRegisterRepository; // Import EventRegisterRepository
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import com.blooddonation.app.config.CustomUserDetails;
import java.util.List;
import com.blooddonation.app.model.*;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository; // Inject DonationRepository
    private final EventRegisterRepository eventRegisterRepository; // Inject EventRegisterRepository
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, DonorRepository donorRepository, DonationRepository donationRepository, EventRegisterRepository eventRegisterRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.donorRepository = donorRepository;
        this.donationRepository = donationRepository; // Initialize DonationRepository
        this.eventRegisterRepository = eventRegisterRepository; // Initialize EventRegisterRepository
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + phone));
        return new CustomUserDetails(user);
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

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User createUser(UserRequest userRequest) {
        User newUser;
        switch (userRequest.getRole()) {
            case IT_OFFICER:
                newUser = new ITOfficer(null, userRequest.getFirstName(), userRequest.getLastName(),
                        LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(),
                        userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(),
                        userRequest.getPassword());
                break;
            case BLOOD_DONATION_MANAGER:
                newUser = new BloodDonationManager(null, userRequest.getFirstName(), userRequest.getLastName(),
                        LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(),
                        userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(),
                        userRequest.getPassword(), null);
                break;
            case HOSPITAL_COORDINATOR:
                newUser = new HospitalCoordinator(null, userRequest.getFirstName(), userRequest.getLastName(),
                        LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(),
                        userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(),
                        userRequest.getPassword(), null);
                break;
            case NURSE:
                newUser = new Nurse(null, userRequest.getFirstName(), userRequest.getLastName(),
                        LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(),
                        userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(),
                        userRequest.getPassword(), null);
                break;
            case DONOR:
                newUser = new Donor(null, userRequest.getFirstName(), userRequest.getLastName(),
                        LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(),
                        userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(),
                        userRequest.getPassword(), null, null, null, null, null); // Added null for eventRegisters
                break;
            case EVENT_ORGANIZER:
                newUser = new EventOrganizer(null, userRequest.getFirstName(), userRequest.getLastName(),
                        LocalDate.parse(userRequest.getDob()), userRequest.getAge(), userRequest.getNic(),
                        userRequest.getPhone(), userRequest.getEmail(), userRequest.getGender(),
                        userRequest.getPassword(), null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + userRequest.getRole());
        }

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
        existingUser.setNic(userRequest.getNic()); // Ensure NIC is updated
        existingUser.setPhone(userRequest.getPhone());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setGender(userRequest.getGender());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // If the user is a Donor, delete associated donations and event registrations first
        if (userToDelete instanceof Donor) {
            Donor donor = (Donor) userToDelete;
            // Delete associated Donation records
            donationRepository.deleteByDonor(donor);
            // Delete associated EventRegister records
            eventRegisterRepository.deleteByDonor(donor);
        }
        // Add similar logic for other user types if they have dependent entities
        // For example, if an EventOrganizer has events, delete events first.
        // For now, focusing on the reported issue with Donor and Donation.

        userRepository.deleteById(id);
    }

    public List<Donor> searchDonorsByFirstName(String firstName) {
        return donorRepository.findByFirstNameContainingIgnoreCase(firstName);
    }
}
