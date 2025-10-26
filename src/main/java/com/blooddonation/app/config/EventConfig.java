package com.blooddonation.app.config;

import com.blooddonation.app.service.EventService;
import com.blooddonation.app.strategy.EventValidationStrategy;
import com.blooddonation.app.strategy.FutureEventDateValidation;
import com.blooddonation.app.strategy.EventCapacityValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public EventValidationStrategy futureEventDateValidation() {
        return new FutureEventDateValidation();
    }

    @Bean
    public EventValidationStrategy eventCapacityValidation() {
        return new EventCapacityValidation();
    }

}
