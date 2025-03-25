package com.digicert.drz.splitio;

import com.digicert.drz.splitio.controller.FeatureFlagController;
import com.digicert.drz.splitio.service.FeatureFlagService;
import io.split.client.SplitClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {FeatureFlagController.class, FeatureFlagService.class})
public class FeatureFlagControllerTest {

    @Autowired
    private FeatureFlagController featureFlagController;

    @MockBean
    private FeatureFlagService featureFlagService;

    @MockBean
    private SplitClient splitClient;

    private MockMvc mockMvc;

    @Test
    public void testCheckFeature() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(featureFlagController).build();

        // Test when logging_level_flag is set to DEBUG
        Mockito.when(featureFlagService.getLoggingLevel("user1")).thenReturn("DEBUG");
        mockMvc.perform(get("/api/feature")
                        .param("userKey", "user1")
                        .param("featureName", "logging_level_flag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF | Logging level is set to: DEBUG"));

        // Test when logging_level_flag is set to INFO
        Mockito.when(featureFlagService.getLoggingLevel("user1")).thenReturn("INFO");
        mockMvc.perform(get("/api/feature")
                        .param("userKey", "user1")
                        .param("featureName", "logging_level_flag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF | Logging level is set to: INFO"));

        // Test the default feature flag "drz_logging_level_flag" behavior for a generic feature (not logging related)
        Mockito.when(featureFlagService.isFeatureEnabled("user1", "drz_logging_level_flag")).thenReturn(true);
        mockMvc.perform(get("/api/feature")
                        .param("userKey", "user1")
                        .param("featureName", "drz_logging_level_flag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is ON"));

        // Test when the feature flag is off for "drz_logging_level_flag"
        Mockito.when(featureFlagService.isFeatureEnabled("user1", "drz_logging_level_flag")).thenReturn(false);
        mockMvc.perform(get("/api/feature")
                        .param("userKey", "user1")
                        .param("featureName", "drz_logging_level_flag"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF"));
    }
}
