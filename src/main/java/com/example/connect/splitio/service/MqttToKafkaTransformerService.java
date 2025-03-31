package com.example.connect.splitio.service;

import io.split.client.SplitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MqttToKafkaTransformerService {

    private static final Logger logger = LoggerFactory.getLogger(MqttToKafkaTransformerService.class);
    private final SplitClient splitClient;

    @Autowired
    public MqttToKafkaTransformerService(SplitClient splitClient) {
        this.splitClient = splitClient;
    }

    public void performAction(String userKey) {
        logger.info("Starting performAction for userKey: {}", userKey);

        String treatment = splitClient.getTreatment(userKey, "service_feature_flag");
        logger.debug("Retrieved treatment for userKey {}: {}", userKey, treatment);

        if ("on".equals(treatment)) {
            logger.debug("Feature flag is ON. Performing action in DEBUG mode.");
            // Perform action when feature flag is ON
        } else {
            logger.info("Feature flag is OFF. Performing action in INFO mode.");
            // Perform action when feature flag is OFF
        }

        logger.info("Completed performAction for userKey: {}", userKey);
    }
}
