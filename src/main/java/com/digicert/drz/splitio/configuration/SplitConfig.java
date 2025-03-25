package com.digicert.drz.splitio.configuration;

import io.split.client.SplitClient;
import io.split.client.SplitClientConfig;
import io.split.client.SplitFactory;
import io.split.client.SplitFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Split.io Configuration to initialize the Split client.
 */
@Slf4j
@Configuration
public class SplitConfig {

    @Value("${split.apiKey}")
    private String apiKey;

    /**
     * Bean to initialize SplitClient.
     * @return SplitClient instance.
     * @throws Exception if there are issues during Split.io client initialization.
     */
    @Bean
    public SplitClient splitClient() throws Exception {
        log.info("Initializing SplitClient with API key: {}", apiKey);

        SplitClientConfig config = SplitClientConfig.builder()
                .setBlockUntilReadyTimeout(10000)
                .build();
        SplitFactory splitFactory = SplitFactoryBuilder.build(apiKey, config);
        SplitClient client = splitFactory.client();
        client.blockUntilReady();

        log.info("SplitClient initialized successfully");
        return client;
    }
}
