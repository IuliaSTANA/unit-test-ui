# Data

This folder stores the "stuff" needed to execute tests and verify behavior.

## Subdirectories
Organize files by feature: `Data/<FeatureName>/`.

## Content Types

1. **Payloads**: JSON/XML files for Faker API requests.
2. **Contract Snapshots**: Examples of actual API Requests and Responses to help compare platform behavior or debug differences.
3. **Local Files**: Example configuration files, local settings, or assets.

## Usage
Reference these files in Feature Files or Preconditions by their relative path from the repository root:
`# Data: Data/Cellwarn/active-notification.json`
