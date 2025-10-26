package com.blooddonation.app.controller;

import com.blooddonation.app.dto.UserRequest;
import com.blooddonation.app.model.User;
import com.blooddonation.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.blooddonation.app.model.Donor; // Import Donor

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/donors/search")
    @PreAuthorize("hasAnyRole('NURSE', 'BLOOD_DONATION_MANAGER', 'IT_OFFICER')") // Added IT_OFFICER for broader access if needed
    public ResponseEntity<List<Donor>> searchDonors(@RequestParam String firstName) {
        List<Donor> donors = userService.searchDonorsByFirstName(firstName);
        return ResponseEntity.ok(donors);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('IT_OFFICER', 'BLOOD_DONATION_MANAGER')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('IT_OFFICER', 'BLOOD_DONATION_MANAGER')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('IT_OFFICER', 'BLOOD_DONATION_MANAGER')")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest userRequest) {
        User newUser = userService.createUser(userRequest);
        System.out.println("User created successfully with ID: " + newUser.getUserId());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('IT_OFFICER', 'BLOOD_DONATION_MANAGER') or #id == authentication.principal.userId")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        User updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('IT_OFFICER', 'BLOOD_DONATION_MANAGER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
