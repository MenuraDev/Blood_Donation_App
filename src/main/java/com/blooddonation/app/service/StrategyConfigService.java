package com.blooddonation.app.service;

import com.blooddonation.app.model.StrategyConfig;
import com.blooddonation.app.repository.StrategyConfigRepository;
import com.blooddonation.app.strategy.BloodRequestValidationStrategy;
import com.blooddonation.app.strategy.DonationEligibilityStrategy;
import com.blooddonation.app.strategy.EventValidationStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StrategyConfigService {

    private final StrategyConfigRepository strategyConfigRepository;
    private final ApplicationContext applicationContext;

    // Maps to hold all available strategies by type
    private Map<String, DonationEligibilityStrategy> donationEligibilityStrategies;
    private Map<String, EventValidationStrategy> eventValidationStrategies;
    private Map<String, BloodRequestValidationStrategy> bloodRequestValidationStrategies;

    @Autowired
    public StrategyConfigService(StrategyConfigRepository strategyConfigRepository, ApplicationContext applicationContext) {
        this.strategyConfigRepository = strategyConfigRepository;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        // Initialize maps with all available strategy beans from the application context
        this.donationEligibilityStrategies = applicationContext.getBeansOfType(DonationEligibilityStrategy.class);
        this.eventValidationStrategies = applicationContext.getBeansOfType(EventValidationStrategy.class);
        this.bloodRequestValidationStrategies = applicationContext.getBeansOfType(BloodRequestValidationStrategy.class);

        // Initialize default strategies in the database if they don't exist
        initializeDefaultStrategy("DONATION_ELIGIBILITY", "minimumAgeEligibility");
        initializeDefaultStrategy("EVENT_VALIDATION", "futureEventDateValidation");
        initializeDefaultStrategy("BLOOD_REQUEST_VALIDATION", "bloodTypeValidation");
    }

    private void initializeDefaultStrategy(String strategyType, String defaultStrategyName) {
        Optional<StrategyConfig> existingConfig = strategyConfigRepository.findByStrategyType(strategyType);
        if (existingConfig.isEmpty()) {
            StrategyConfig newConfig = new StrategyConfig(strategyType, defaultStrategyName);
            strategyConfigRepository.save(newConfig);
        }
    }

    public StrategyConfig getStrategyConfig(String strategyType) {
        return strategyConfigRepository.findByStrategyType(strategyType)
                .orElseThrow(() -> new RuntimeException("Strategy configuration not found for type: " + strategyType));
    }

    public StrategyConfig updateActiveStrategy(String strategyType, String activeStrategyName) {
        // Validate if the activeStrategyName is a valid bean for the given strategyType
        if (!isValidStrategyName(strategyType, activeStrategyName)) {
            throw new IllegalArgumentException("Invalid strategy name '" + activeStrategyName + "' for type '" + strategyType + "'");
        }

        StrategyConfig config = strategyConfigRepository.findByStrategyType(strategyType)
                .orElseGet(() -> new StrategyConfig(strategyType, activeStrategyName)); // Create if not exists
        config.setActiveStrategyName(activeStrategyName);
        return strategyConfigRepository.save(config);
    }

    public String getActiveStrategyName(String strategyType) {
        return strategyConfigRepository.findByStrategyType(strategyType)
                .map(StrategyConfig::getActiveStrategyName)
                .orElseThrow(() -> new RuntimeException("Active strategy not configured for type: " + strategyType));
    }

    public DonationEligibilityStrategy getActiveDonationEligibilityStrategy() {
        String activeStrategyName = getActiveStrategyName("DONATION_ELIGIBILITY");
        return donationEligibilityStrategies.get(activeStrategyName);
    }

    public EventValidationStrategy getActiveEventValidationStrategy() {
        String activeStrategyName = getActiveStrategyName("EVENT_VALIDATION");
        return eventValidationStrategies.get(activeStrategyName);
    }

    public BloodRequestValidationStrategy getActiveBloodRequestValidationStrategy() {
        String activeStrategyName = getActiveStrategyName("BLOOD_REQUEST_VALIDATION");
        return bloodRequestValidationStrategies.get(activeStrategyName);
    }

    public Set<String> getAvailableStrategyNames(String strategyType) {
        switch (strategyType) {
            case "DONATION_ELIGIBILITY":
                return donationEligibilityStrategies.keySet();
            case "EVENT_VALIDATION":
                return eventValidationStrategies.keySet();
            case "BLOOD_REQUEST_VALIDATION":
                return bloodRequestValidationStrategies.keySet();
            default:
                throw new IllegalArgumentException("Unknown strategy type: " + strategyType);
        }
    }

    private boolean isValidStrategyName(String strategyType, String strategyName) {
        return getAvailableStrategyNames(strategyType).contains(strategyName);
    }

    public List<StrategyConfig> getAllStrategyConfigs() {
        return strategyConfigRepository.findAll();
    }
}
