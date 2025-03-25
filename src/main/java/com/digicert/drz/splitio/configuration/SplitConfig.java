package com.digicert.drz.splitio.configuration;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Configuration
public class SplitConfig {

    private static final Logger logger = LoggerFactory.getLogger(SplitConfig.class);

    @Value("${split.apiKey}")
    private String apiKey;

    @Bean
    public SplitClient splitClient() throws Exception {
        logger.info("Initializing SplitClient with API key: {}", apiKey);

        SplitClientConfig config = SplitClientConfig.builder()
                .setBlockUntilReadyTimeout(10000)
                .build();
        SplitFactory splitFactory = SplitFactoryBuilder.build(apiKey, config);
        SplitClient client = splitFactory.client();
        client.blockUntilReady();

        // Test the API key by fetching a feature flag
        String testFeature = "drz_feature";
        String treatment = client.getTreatment("user", testFeature);
        logger.info("Test feature flag treatment: {}", treatment);

        logger.info("SplitClient initialized successfully");
        logger.info("SplitClient configuration: BlockUntilReadyTimeout = {}", config.blockUntilReady());
        return client;
    }
}
