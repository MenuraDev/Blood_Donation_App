package com.blooddonation.app.controller;

import com.blooddonation.app.config.CustomUserDetails;
import com.blooddonation.app.model.Donation;
import com.blooddonation.app.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/donations")
public class DonationHistoryController {

    @Autowired
    private DonationService donationService;

    @GetMapping("/history")
    public String getDonationHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            // The client-side JavaScript will fetch the donations using the API.
            // No need to add donations to the model here.
        } else {
            // Handle cases where user is not authenticated or not a CustomUserDetails
            // For now, redirect to login or show an error
            return "redirect:/login";
        }
        return "donation-history";
    }
}
