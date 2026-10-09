# Test-Plan Repository

This repository is the single source of truth for feature behaviors, test setups, and data examples used by both Android and iOS teams.

## Structure

* **[FeatureFiles/](FeatureFiles/README.md)**: Gherkin `.feature` files describing observable user behavior.
* **[Preconditions/](Preconditions/README.md)**: Markdown files with reusable state blocks and setup shortcuts.
* **[Data/](Data/README.md)**: Example payloads, API request/response snapshots, and configuration files.

## Workflow

1. **Refinement**: Create a scenario skeleton in `FeatureFiles/`.
2. **Development**: Update scenarios with details. Add `Preconditions/` and `Data/` as they are discovered.
3. **Merge**: The feature file is the source of truth for QA review and manual/automated testing.
