# SplitIo-POC

## Overview
This POC application demonstrates how to use Split.io to manage Feature Flags in a Java application. The goal is to understand the Split.io framework's functionality for Feature Flags, allowing us to add new functionality for a limited scope of requests or enable/disable certain features without redeploying the cluster.

## Use Case
For example, we can temporarily switch the application's logging level from INFO to DEBUG when debugging some issues.

## Technology Stack
- Java
- Spring Boot
- Gradle
- Split.io Java SDK

## Package
- `com.example.connect.splitio.poc`

## Reference Link
[Split.io Java SDK Documentation](https://help.split.io/hc/en-us/articles/360020405151-Java-SDK)

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/prasadDasari/SplitIo-POC.git
   cd SplitIo-POC
   2. **Build the application:**
   ./gradlew clean build  // To build the application with no compilation errors etc

   3. **Run the application:** 
   ./gradlew bootRun --args='--spring.profiles.active=local' // To run the application locally


![AppLogs-Startup.png](assets/AppLogs-Startup.png)

##### Necessary Files as a part of Implementation
To integrate Split.io into the project, add the following files:

**SplitConfig.java**
This file contains the configuration for Split.io.
**FeatureFlagService.java**
This file contains the service to interact with Split.io feature flags.
**FeatureFlagController.java**
This file contains the controller to expose an API endpoint for checking feature flags.
**application-local.yaml**
This file is a configuration file used in Spring Boot applications to define environment-specific properties.
It includes settings such as the application name, active profile, server port, and the Split.io API key.


2 **Configure Split.io:**
   Log in to your Split.io account, after successful Registration.
   Navigate to the "API Keys" section in the Split.io dashboard.
   Generate and copy the API key for your environment.
   Update the application-local.yaml file with your Split.io API key.

3 **Usage**
Use Split.io to create and manage feature flags.
Modify the application to check the status of feature flags and enable/disable features accordingly.


### Testing Feature Flags after creating them in Split.io
1. Create a feature flag in Split.io. Ex: `env_logging_level_flag` created for this poc.
2. Configure Treatments and necessary config for logging level, as per screenshot, Click `Review` Changes and Click `Save` it


![FeatureFlag-SetupForLogging.png](assets/FeatureFlag-SetupForLogging.png)

3. Run the spring boot DemoSplitIOApplication.
4. Access the API endpoint to check the status of the feature flag: (Ex: http://localhost:8080/feature-flag/{feature-flag-name})
   Check `Curl Endpoints` provided below
5. Verify that the feature flag is working as expected
6. Modify the feature flag in Split.io to see the changes reflected in the application.
7. Test the application again to verify that the feature flag changes are working as expected.

### Curl Endpoints
Please Note: Default logging level is INFO.
curl -G "http://localhost:8080/api/feature" --data-urlencode "userKey=user" --data-urlencode "featureName=env_logging_level_flag"


![curl-debug-OFF.png](assets/curl-debug-OFF.png)

-->Response:  "Feature is OFF%" for Info Level

## Service Logs to check for INFO Logs
![Info-logs-OFF.png](assets/Info-logs-OFF.png)

8. Switch the feature flag to ON in Split.io and test the application again to see the logging level change to DEBUG.
## curl -G "http://localhost:8080/api/feature" --data-urlencode "userKey=user" --data-urlencode "featureName=env_logging_level_flag"

![curl-debug-ON.png](assets/curl-debug-ON.png)

-->  Response "Feature is ON%" For Debug Level

If the feature flag is ON, the logging level will be changed to DEBUG

## ## Service Logs to check for DEBUG Logs
![Debug-logs-ON.png](assets/Debug-logs-ON.png)

## Monitoring ON/OFF Flag : Metrics from Split.io
![Metrics-ON-OFF-CALLS.png](assets/Metrics-ON-OFF-CALLS.png)

### Conclusion
Split.io is a powerful tool for managing feature flags in applications. It allows developers to control the behavior of their applications without redeploying the codebase. 
By integrating Split.io into the project, developers can easily create, manage and test feature flags to enable/disable features based on specific conditions. 
This POC application demonstrates how to use Split.io to manage feature flags in a Java application and provides a starting point for integrating Split.io into other projects.

### Note:
Alert Baseline Treatment to ensure INFO logging is the default when the flag is killed,
while the Default Treatment would help set the logging level for users 
who aren't specifically targeted by the flag.

### Testing 
gp2-demo: Feature Switched to DEBUG in SplitIO UI, by setting to ON, ByDefault it is OFF.

### pre-requisite: Segment Creation for each environment
Add demo-user1 and demo-user2 to GP2-Demo Segment in the SplitIO UI.
Add prod-user1 and prod-user2 to GP2-Prod Segment in SplitIO UI.

1. Environment Flag : env_logging_level_flag
curl -X GET "http://localhost:8080/api/feature/env?userKey=demo-user1&featureName=env_logging_level_flag"
Feature is ON | Logging level is set to: DEBUG

curl -X GET "http://localhost:8080/api/feature/env?userKey=demo-user2&featureName=env_logging_level_flag"
Feature is ON | Logging level is set to: DEBUG

curl -X GET "http://localhost:8080/api/feature/env?userKey=demo-user3&featureName=env_logging_level_flag"
Feature is ON | Logging level is set to: DEBUG

[All users will see same behaviour as DEBUG]


2. Service Flag: connector_service_logging_flag
curl -X GET "http://localhost:8080/api/feature/service?userKey=demo-user1&featureName=connector_service_logging_flag"
Feature is ON | Logging level is set to: DEBUG

curl -X GET "http://localhost:8080/api/feature/service?userKey=demo-user2&featureName=connector_service_logging_flag"
Feature is ON | Logging level is set to: DEBUG

ONLY users in segment (demo-user1 and demo-user2 ) can see DEBUG logs

curl -X GET "http://localhost:8080/api/feature/service?userKey=demo-user3&featureName=connector_service_logging_flag"
Feature is OFF | Logging level is set to: INFO

[Users not part of segment, can only see INFO logs, as the Feature is OFF as per targeting rules set]

3. Service Flag: mqtt_to_kafka_service_logging_flag
curl -X GET "http://localhost:8080/api/feature/service?userKey=demo-user1&featureName=mqtt_to_kafka_service_logging_flag" 
Feature is ON | Logging level is set to: DEBUG%

url -X GET "http://localhost:8080/api/feature/service?userKey=demo-user2&featureName=mqtt_to_kafka_service_logging_flag"
Feature is ON | Logging level is set to: DEBUG

[ONLY users in segment (demo-user1 and demo-user2 ) can see DEBUG logs]

curl -X GET "http://localhost:8080/api/feature/service?userKey=demo-user3&featureName=mqtt_to_kafka_service_logging_flag"
Feature is OFF | Logging level is set to: INFO

[Users not part of segment, can only see INFO logs, as the Feature is OFF as per targeting rules set]

### Some notes

The two options
Individual Targets and Add Attribute Based Targeting Rules, are different ways to define which users or groups of users should be served a specific feature treatment
(i.e., the feature flag's state).
Here’s the difference and when to use each:

1. Individual Targets:
* What it is: This option allows you to target specific users (e.g., "user1", "user2") directly by providing their unique identifiers. You can assign the treatment (e.g., on or off) to specific individuals.
* Use case:
   * When you want to target specific users directly, such as internal users or users who need special access to certain features (e.g., for testing or internal purposes).
   * Example: You want to enable a feature for a specific user like "prod-user1" but keep it off for others.
* How to configure:
   * Add individual users directly to the targeting rule and assign them a treatment.
2. Add Attribute Based Targeting Rules:
* What it is: This option allows you to create more dynamic targeting rules based on user attributes. These attributes could be anything about the user (e.g., region, account type, subscription status, etc.). The feature flag is served based on these attributes, which gives you more flexibility for large-scale targeting.
* Use case:
   * When you want to apply feature flags based on user characteristics such as geography (e.g., serve a feature to all users in a specific country), account type (e.g., premium users), or any other custom user attributes.
   * Example: You want to enable a feature for users who have a "premium" account or users in a specific region.
* How to configure:
   * You can use the conditions like Is in segment, Is not in flag, or Is in list to determine whether the user qualifies for the feature treatment. These rules let you control which set of users get the feature enabled based on a broader context (e.g., account status, user attributes).
     When to Choose What?
* Choose Individual Targets:
   * When you need precise control over specific users (e.g., internal testing, early access for some users, etc.).
   * Example: "Enable this feature for user1 only."
* Choose Attribute Based Targeting Rules:
   * When you need to target users in a more scalable way based on specific attributes or conditions.
   * Example: "Enable this feature for all users in the US region" or "Enable this for all premium users."
     Visual Explanation from Screenshots:
* First Screenshot: You're defining an individual target for a specific user (e.g., prod-user1). This directly assigns a treatment (on or off) to the individual user.
* Second Screenshot: You're using Attribute Based Targeting Rules and specifying conditions, such as whether a user is in a certain segment. This would let you apply the flag to users who fit that condition.
  Let me know if you'd like further clarification or assistance in setting these up in your system.
