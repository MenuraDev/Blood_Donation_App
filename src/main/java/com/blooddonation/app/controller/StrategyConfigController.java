package com.blooddonation.app.controller;

import com.blooddonation.app.model.StrategyConfig;
import com.blooddonation.app.service.StrategyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/strategies")
public class StrategyConfigController {

    private final StrategyConfigService strategyConfigService;

    @Autowired
    public StrategyConfigController(StrategyConfigService strategyConfigService) {
        this.strategyConfigService = strategyConfigService;
    }

    @GetMapping("/config")
    @PreAuthorize("hasRole('IT_OFFICER')")
    public ResponseEntity<List<StrategyConfig>> getAllStrategyConfigs() {
        return ResponseEntity.ok(strategyConfigService.getAllStrategyConfigs());
    }

    @GetMapping("/available/{strategyType}")
    @PreAuthorize("hasRole('IT_OFFICER')")
    public ResponseEntity<Set<String>> getAvailableStrategyNames(@PathVariable String strategyType) {
        return ResponseEntity.ok(strategyConfigService.getAvailableStrategyNames(strategyType.toUpperCase()));
    }

    @PutMapping("/config/{strategyType}")
    @PreAuthorize("hasRole('IT_OFFICER')")
    public ResponseEntity<StrategyConfig> updateActiveStrategy(
            @PathVariable String strategyType,
            @RequestBody Map<String, String> requestBody) {
        String activeStrategyName = requestBody.get("activeStrategyName");
        if (activeStrategyName == null || activeStrategyName.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        StrategyConfig updatedConfig = strategyConfigService.updateActiveStrategy(strategyType.toUpperCase(), activeStrategyName);
        return ResponseEntity.ok(updatedConfig);
    }
}
