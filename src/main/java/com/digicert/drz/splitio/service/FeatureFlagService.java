package com.digicert.drz.splitio.service;

import io.split.client.SplitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    @Autowired
    private SplitClient splitClient;

    public boolean isFeatureEnabled(String userKey, String featureName) {
        return splitClient.getTreatment(userKey, featureName).equals("on");
    }
}
