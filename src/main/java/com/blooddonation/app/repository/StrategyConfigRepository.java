package com.blooddonation.app.repository;

import com.blooddonation.app.model.StrategyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StrategyConfigRepository extends JpaRepository<StrategyConfig, Long> {
    Optional<StrategyConfig> findByStrategyType(String strategyType);
}
