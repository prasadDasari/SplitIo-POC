package com.digicert.drz.splitio.service;

import io.split.client.SplitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer to manage feature flag evaluations.
 */
@Service
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
        String featureFlagName = "drz_logging_level_flag";  // The feature flag controlling logging level

        // Fetch the treatment (DEBUG or INFO)
        String treatment = splitClient.getTreatment(userKey, featureFlagName);

        if ("DEBUG".equals(treatment)) {
            return "DEBUG";  // Enable DEBUG level logging
        }
        return "INFO";  // Default to INFO level logging
    }

    /**
     * Check if a specific feature flag is enabled for a user.
     * @param userKey The unique key for the user.
     * @param featureName The feature flag name (e.g., "drz_feature").
     * @return true if the feature is enabled, false otherwise.
     */
    public boolean isFeatureEnabled(String userKey, String featureName) {
        String treatment = splitClient.getTreatment(userKey, featureName);
        return "on".equals(treatment);
    }
}
