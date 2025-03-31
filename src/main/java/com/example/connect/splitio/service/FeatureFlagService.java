package com.example.connect.splitio.service;

import io.split.client.SplitClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer to manage feature flag evaluations.
 */
@Service
@Slf4j
public class FeatureFlagService {

    private final SplitClient splitClient;

    @Autowired
    public FeatureFlagService(SplitClient splitClient) {
        this.splitClient = splitClient;
    }

    /**
     * Fetch the logging level based on the Split.io feature flag.
     * @param userKey The unique key for the user or request context.
     * @return the logging level (DEBUG or INFO).
     */
    public String getLoggingLevel(String userKey) {
        String featureFlagName = "env_logging_level_flag";  // The feature flag controlling logging level

        log.debug("Fetching logging level for user '{}', using feature flag '{}'", userKey, featureFlagName);
        // Fetch the treatment (DEBUG or INFO)
        String treatment = splitClient.getTreatment(userKey, featureFlagName);
        log.debug("Fetched treatment for user '{}': {}", userKey, treatment);

        if ("on".equals(treatment)) {
            log.debug("Setting logging level to DEBUG for user '{}'", userKey);
            return "DEBUG";  // Enable DEBUG level logging
        }
        log.debug("Setting logging level to INFO for user '{}'", userKey);
        return "INFO";  // Default to INFO level logging
    }

    /**
     * Check if a specific feature flag is enabled for a user.
     * @param userKey The unique key for the user.
     * @param featureName The feature flag name (e.g., "env_feature").
     * @return true if the feature is enabled, false otherwise.
     */
    public boolean isFeatureEnabled(String userKey, String featureName) {
        log.debug("Checking if feature '{}' is enabled for user '{}'", featureName, userKey);
        String treatment = splitClient.getTreatment(userKey, featureName);

        log.debug("Fetched treatment for feature '{}', user '{}': {}", featureName, userKey, treatment);
        boolean isEnabled = "on".equals(treatment);
        log.debug("Feature '{}' for user '{}' is {}", featureName, userKey, isEnabled ? "enabled" : "disabled");
        return isEnabled;
    }
}
