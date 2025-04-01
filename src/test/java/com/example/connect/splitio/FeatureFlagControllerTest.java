package com.example.connect.splitio;

import com.example.connect.splitio.controller.FeatureFlagController;
import com.example.connect.splitio.enums.FeatureFlag;
import com.example.connect.splitio.service.ConnectorService;
import com.example.connect.splitio.service.FeatureFlagService;
import com.example.connect.splitio.service.MqttToKafkaTransformerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Method;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {FeatureFlagController.class, FeatureFlagService.class})
class FeatureFlagControllerTest {

    @Autowired
    private FeatureFlagController featureFlagController;

    @MockBean
    private FeatureFlagService featureFlagService;

    @MockBean
    private ConnectorService connectorService;

    @MockBean
    private MqttToKafkaTransformerService mqttToKafkaTransformerService;

    @MockBean
    private LoggingSystem loggingSystem;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(featureFlagController).build();
    }

    @Test
    void testCheckEnvFeature() throws Exception {
        // Test when logging_level_flag is set to DEBUG
        Mockito.when(featureFlagService.getLoggingLevel("user1", FeatureFlag.ENV_LOGGING_LEVEL_FLAG)).thenReturn("on");
        mockMvc.perform(get("/api/feature/env")
                        .param("userKey", "user1")
                        .param("featureName", FeatureFlag.ENV_LOGGING_LEVEL_FLAG.getFlagName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is ON | Logging level is set to: DEBUG"));

        // Test when the feature flag is off for "env_logging_level_flag"
        Mockito.when(featureFlagService.getLoggingLevel("user1", FeatureFlag.ENV_LOGGING_LEVEL_FLAG)).thenReturn("off");
        mockMvc.perform(get("/api/feature/env")
                        .param("userKey", "user1")
                        .param("featureName", FeatureFlag.ENV_LOGGING_LEVEL_FLAG.getFlagName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF | Logging level is set to: INFO"));
    }

    @Test
    void testCheckServiceFeature() throws Exception {
        // Test when the feature flag is on for "connector_service_logging_flag"
        Mockito.when(featureFlagService.getLoggingLevel("user1", FeatureFlag.CONNECTOR_SERVICE_LOGGING_FLAG)).thenReturn("on");
        mockMvc.perform(get("/api/feature/service")
                        .param("userKey", "user1")
                        .param("featureName", FeatureFlag.CONNECTOR_SERVICE_LOGGING_FLAG.getFlagName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is ON | Logging level is set to: DEBUG"));

        // Test when the feature flag is off for "connector_service_logging_flag"
        Mockito.when(featureFlagService.getLoggingLevel("user1", FeatureFlag.CONNECTOR_SERVICE_LOGGING_FLAG)).thenReturn("off");
        mockMvc.perform(get("/api/feature/service")
                        .param("userKey", "user1")
                        .param("featureName", FeatureFlag.CONNECTOR_SERVICE_LOGGING_FLAG.getFlagName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF | Logging level is set to: INFO"));

        // Test when the feature flag is on for "mqtt_to_kafka_service_logging_flag"
        Mockito.when(featureFlagService.getLoggingLevel("user1", FeatureFlag.MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG)).thenReturn("on");
        mockMvc.perform(get("/api/feature/service")
                        .param("userKey", "user1")
                        .param("featureName", FeatureFlag.MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG.getFlagName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is ON | Logging level is set to: DEBUG"));

        // Test when the feature flag is off for "mqtt_to_kafka_service_logging_flag"
        Mockito.when(featureFlagService.getLoggingLevel("user1", FeatureFlag.MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG)).thenReturn("off");
        mockMvc.perform(get("/api/feature/service")
                        .param("userKey", "user1")
                        .param("featureName", FeatureFlag.MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG.getFlagName()))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF | Logging level is set to: INFO"));

        // Verify that performAction is called for ConnectorService
        Mockito.verify(connectorService, Mockito.times(1)).performAction("user1", FeatureFlag.CONNECTOR_SERVICE_LOGGING_FLAG.getFlagName());

        // Verify that performAction is called for MqttToKafkaTransformerService
        Mockito.verify(mqttToKafkaTransformerService, Mockito.times(1)).performAction("user1", FeatureFlag.MQTT_TO_KAFKA_SERVICE_LOGGING_FLAG.getFlagName());
    }

    @Test
    void testAdjustLoggingLevel() throws Exception {
        // Set the logging package value
        ReflectionTestUtils.setField(featureFlagController, "loggingPackage", "com.example.connect.splitio");

        // Access the private method using reflection
        Method adjustLoggingLevelMethod = FeatureFlagController.class.getDeclaredMethod("adjustLoggingLevel", String.class);
        adjustLoggingLevelMethod.setAccessible(true);

        // Test setting logging level to DEBUG
        adjustLoggingLevelMethod.invoke(featureFlagController, "on");
        Mockito.verify(loggingSystem).setLogLevel("com.example.connect.splitio", LogLevel.DEBUG);

        // Test setting logging level to INFO
        adjustLoggingLevelMethod.invoke(featureFlagController, "off");
        Mockito.verify(loggingSystem).setLogLevel("com.example.connect.splitio", LogLevel.INFO);
    }
}
