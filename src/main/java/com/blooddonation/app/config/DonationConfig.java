package com.blooddonation.app.config;

import com.blooddonation.app.service.DonationService;
import com.blooddonation.app.strategy.DonationEligibilityStrategy;
import com.blooddonation.app.strategy.MinimumAgeEligibility;
import com.blooddonation.app.strategy.RecentDonationEligibility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DonationConfig {

    @Bean
    public DonationEligibilityStrategy minimumAgeEligibility() {
        return new MinimumAgeEligibility();
    }

    @Bean
    public DonationEligibilityStrategy recentDonationEligibility() {
        return new RecentDonationEligibility();
    }

}
