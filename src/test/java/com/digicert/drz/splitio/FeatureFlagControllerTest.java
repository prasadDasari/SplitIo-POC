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

        Mockito.when(featureFlagService.isFeatureEnabled("user1", "feature1")).thenReturn(true);

        mockMvc.perform(get("/api/feature")
                        .param("userKey", "user1")
                        .param("featureName", "feature1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is ON"));

        Mockito.when(featureFlagService.isFeatureEnabled("user1", "feature1")).thenReturn(false);

        mockMvc.perform(get("/api/feature")
                        .param("userKey", "user1")
                        .param("featureName", "feature1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is OFF"));
    }
}
