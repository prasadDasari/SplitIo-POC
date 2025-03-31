package com.example.connect.splitio.enums;

public enum FeatureFlag {
    ENV_LOGGING_LEVEL_FLAG("env_logging_level_flag"),

    // SERVICE FLAGS
    CONNECTOR_SERVICE_LOGGING_FLAG("connector_service_logging_flag"),
    MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG("mqtt_to_kafka_service_logging_flag");

    private final String flagName;

    FeatureFlag(String flagName) {
        this.flagName = flagName;
    }

    public String getFlagName() {
        return flagName;
    }
}
