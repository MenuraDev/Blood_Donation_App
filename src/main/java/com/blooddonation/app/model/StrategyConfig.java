package com.blooddonation.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "strategy_config")
public class StrategyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy_type", nullable = false, unique = true)
    private String strategyType; // e.g., "DONATION_ELIGIBILITY", "EVENT_VALIDATION", "BLOOD_REQUEST_VALIDATION"

    @Column(name = "active_strategy_name", nullable = false)
    private String activeStrategyName; // e.g., "minimumAgeEligibility", "recentDonationEligibility"

    // Constructors
    public StrategyConfig() {
    }

    public StrategyConfig(String strategyType, String activeStrategyName) {
        this.strategyType = strategyType;
        this.activeStrategyName = activeStrategyName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public String getActiveStrategyName() {
        return activeStrategyName;
    }

    public void setActiveStrategyName(String activeStrategyName) {
        this.activeStrategyName = activeStrategyName;
    }

    @Override
    public String toString() {
        return "StrategyConfig{" +
               "id=" + id +
               ", strategyType='" + strategyType + '\'' +
               ", activeStrategyName='" + activeStrategyName + '\'' +
               '}';
    }
}
