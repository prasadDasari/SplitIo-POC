package com.digicert.drz.splitio.controller;

import com.digicert.drz.splitio.service.FeatureFlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FeatureFlagController {

    @Autowired
    private FeatureFlagService featureFlagService;

    @GetMapping("/feature")
    public String checkFeature(@RequestParam String userKey, @RequestParam String featureName) {
        boolean isFeatureEnabled = featureFlagService.isFeatureEnabled(userKey, featureName);
        if (isFeatureEnabled) {
            return "Feature is ON";
        } else {
            return "Feature is OFF";
        }
    }
}
