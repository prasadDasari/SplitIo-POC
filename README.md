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
- `com.digicert.drz.splitio.poc`

## Reference Link
[Split.io Java SDK Documentation](https://help.split.io/hc/en-us/articles/360020405151-Java-SDK)

## Setup Instructions

1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd SplitIo-POC

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
- `com.digicert.drz.splitio.poc`

## Reference Link
[Split.io Java SDK Documentation](https://help.split.io/hc/en-us/articles/360020405151-Java-SDK)

## Setup Instructions

1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd SplitIo-POC

2. **Build the application:**  
    ./gradlew build
3.  **Run the application:** 
   ./gradlew bootRun
4. **Configure Split.io:**
   Log in to your Split.io account.
   Navigate to the "API Keys" section in the Split.io dashboard.
   Generate and copy the API key for your environment.
   Update the application-local.yaml file with your Split.io API key.

5. **Usage**
Use Split.io to create and manage feature flags.
Modify the application to check the status of feature flags and enable/disable features accordingly.
Necessary Files
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
