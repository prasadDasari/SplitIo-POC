package com.example.connect.splitio.controller;

import com.example.connect.splitio.enums.FeatureFlag;
import com.example.connect.splitio.service.ConnectorService;
import com.example.connect.splitio.service.FeatureFlagService;
import com.example.connect.splitio.service.MqttToKafkaTransformerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@Slf4j
@RestController
@RequestMapping("/api")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;
    private final LoggingSystem loggingSystem;
    private final ConnectorService connectorService;
    private final MqttToKafkaTransformerService mqttToKafkaTransformerService;

    @Value("${spring.custom.logging.package}")
    private String loggingPackage;

    @Autowired
    public FeatureFlagController(FeatureFlagService featureFlagService, LoggingSystem loggingSystem, ConnectorService connectorService, MqttToKafkaTransformerService mqttToKafkaTransformerService) {
        this.featureFlagService = featureFlagService;
        this.loggingSystem = loggingSystem;
        this.connectorService = connectorService;
        this.mqttToKafkaTransformerService = mqttToKafkaTransformerService;
    }

    @GetMapping("/feature/env")
    public String checkEnvFeature(@RequestParam String userKey, @RequestParam String featureName) {
        log.info("Endpoint '/api/feature/env' hit with userKey: {} and featureName: {}", userKey, featureName);
        log.debug("Received request to check environment feature flag: {} for user: {}", featureName, userKey);

        if (FeatureFlag.ENV_LOGGING_LEVEL_FLAG.getFlagName().equals(featureName)) {
            String treatment = featureFlagService.getLoggingLevel(userKey, FeatureFlag.ENV_LOGGING_LEVEL_FLAG);
            boolean isFeatureEnabled = "on".equals(treatment);
            String featureStatus = isFeatureEnabled ? "Feature is ON" : "Feature is OFF";
            log.debug("Feature flag '{}' for user '{}' is {}", featureName, userKey, featureStatus);

            String loggingLevel = isFeatureEnabled ? "DEBUG" : "INFO";
            adjustLoggingLevel(loggingLevel);
            return featureStatus + " | Logging level is set to: " + loggingLevel;
        } else {
            log.warn("Environment feature flag '{}' does not exist", featureName);
            return "Environment feature flag does not exist";
        }
    }

    @GetMapping("/feature/service")
    public String checkServiceFeature(@RequestParam String userKey, @RequestParam String featureName) {
        log.info("Endpoint '/api/feature/service' hit with userKey: {} and featureName: {}", userKey, featureName);
        log.debug("Received request to check service feature flag: {} for user: {}", featureName, userKey);

        if (FeatureFlag.CONNECTOR_SERVICE_LOGGING_FLAG.getFlagName().equals(featureName)) {
            return executeServiceAction(userKey, featureName, (key) -> connectorService.performAction(key, featureName));
        } else if (FeatureFlag.MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG.getFlagName().equals(featureName)) {
            return executeServiceAction(userKey, featureName, (key) -> mqttToKafkaTransformerService.performAction(key, featureName));
        } else {
            log.warn("Service feature flag '{}' does not exist", featureName);
            return "Service feature flag does not exist";
        }
    }

    private String executeServiceAction(String userKey, String featureName, Consumer<String> action) {
        FeatureFlag featureFlag = getFeatureFlagByName(featureName);
        if (featureFlag == null) {
            log.warn("Feature flag '{}' does not exist", featureName);
            return "Feature flag does not exist";
        }

        String treatment = featureFlagService.getLoggingLevel(userKey, featureFlag);
        if ("on".equals(treatment)) {
            action.accept(userKey);
            return "Feature is ON";
        } else {
            log.info("Feature flag '{}' is OFF. No action performed.", featureName);
            return "Feature is OFF";
        }
    }

    private FeatureFlag getFeatureFlagByName(String flagName) {
        for (FeatureFlag featureFlag : FeatureFlag.values()) {
            if (featureFlag.getFlagName().equals(flagName)) {
                return featureFlag;
            }
        }
        return null;
    }

    private void adjustLoggingLevel(String loggingLevel) {
        if ("on".equals(loggingLevel)) {
            loggingSystem.setLogLevel(loggingPackage, LogLevel.DEBUG);
            log.debug("Logging level set to DEBUG");
        } else {
            loggingSystem.setLogLevel(loggingPackage, LogLevel.INFO);
            log.info("Logging level set to INFO");
        }
    }
}
