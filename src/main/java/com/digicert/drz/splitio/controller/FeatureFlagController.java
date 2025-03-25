package com.digicert.drz.splitio.controller;

import com.digicert.drz.splitio.service.FeatureFlagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to expose endpoints for feature flag status and dynamic logging level change.
 */
@Slf4j
@RestController
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @Autowired
    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    /**
     * Endpoint to check the status of a feature flag and adjust logging level.
     * @param userKey The unique identifier for the user or request context.
     * @param featureName The name of the feature flag (e.g., "logging_level_flag").
     * @return the current feature flag status and the adjusted logging level.
     */
    @GetMapping("/feature")
    public String checkFeature(@RequestParam String userKey, @RequestParam String featureName) {
        // Check if the feature is enabled (e.g., "drz_logging_level_flag")
        boolean isFeatureEnabled = featureFlagService.isFeatureEnabled(userKey, featureName);
        String featureStatus = isFeatureEnabled ? "Feature is ON" : "Feature is OFF";

        // Adjust logging level dynamically based on the 'logging_level_flag'
        if ("logging_level_flag".equals(featureName)) {
            String loggingLevel = featureFlagService.getLoggingLevel(userKey);
            adjustLoggingLevel(loggingLevel);
            return featureStatus + " | Logging level is set to: " + loggingLevel;
        }

        return featureStatus;
    }

    /**
     * Adjust the logging level based on the value of the 'logging_level_flag'.
     * @param loggingLevel The logging level to set (DEBUG or INFO).
     */
    private void adjustLoggingLevel(String loggingLevel) {
        if ("DEBUG".equals(loggingLevel)) {
            // Change logging level to DEBUG
            log.debug("Logging level set to DEBUG");
        } else {
            // Change logging level to INFO
            log.info("Logging level set to INFO");
        }
    }
}
