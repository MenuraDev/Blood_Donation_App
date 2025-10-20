package com.blooddonation.app.config;

import com.blooddonation.app.service.BloodRequestService;
import com.blooddonation.app.strategy.BloodRequestValidationStrategy;
import com.blooddonation.app.strategy.BloodTypeValidation;
import com.blooddonation.app.strategy.PositiveQuantityValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BloodRequestConfig {

    @Bean
    public BloodRequestValidationStrategy bloodTypeValidation() {
        return new BloodTypeValidation();
    }

    @Bean
    public BloodRequestValidationStrategy positiveQuantityValidation() {
        return new PositiveQuantityValidation();
    }

}
