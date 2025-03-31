package com.example.connect.splitio.service;

import com.example.connect.splitio.enums.FeatureFlag;
import io.split.client.SplitClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FeatureFlagService {

    private final SplitClient splitClient;

    @Value("${split.environment}")
    private String environment;

    @Autowired
    public FeatureFlagService(SplitClient splitClient) {
        this.splitClient = splitClient;
    }

    public String getLoggingLevel(String userKey, FeatureFlag featureFlag) {
        log.debug("Checking treatment for feature flag '{}' and user '{}' in environment '{}'", featureFlag.getFlagName(), userKey, environment);
        String treatment = splitClient.getTreatment(userKey, featureFlag.getFlagName());
        log.debug("Treatment for feature flag '{}' and user '{}' in environment '{}' is '{}'", featureFlag.getFlagName(), userKey, environment, treatment);
        if ("control".equals(treatment)) {
            log.warn("Feature flag '{}' does not exist in the current environment '{}'", featureFlag.getFlagName(), environment);
            return "off"; // Default to "off" when the feature flag does not exist
        }
        return treatment;
    }
}
