# Feature Files

This folder contains Gherkin `.feature` files. Each file describes the observable behavior of a specific feature or epic.

## Writing Conventions

1. **One Scenario = One Behavior**: Keep scenarios focused on a single outcome.
2. Scenarios are **mutually exclusive** and **independent**:
    * **No Chaining**: A scenario must never rely on a previous scenario having finished. You should be able to run any scenario in isolation, in any order.
    * **Atomic Outcomes**: Each scenario tests one specific "fork" in the road. Don't test the whole flow in one scenario; test the transition from one specific state to the next.
    * **State Teleportation**: Use `Given` steps and `@Preconditions` to "teleport" the app into the exact state needed for the `When` action.
    * **Example**: If testing "Change Password," the `Given` should assume the user is already logged in and on the settings screen. Do not include the "Login" or "Navigate to Settings" steps in this scenario.
3. **Stable IDs**: Use `@AA-xx` tags (e.g., `@CW-01`).
    * **Never** change or renumber an ID once it's assigned.
    * The ids are unique across all feature files.
    * It's easiest to keep the letter prefixes unique to a feature file (some abbreviation of the feature, e.g. `Cellwarn.feature` -> `@CW-01`)
4. **Tagging**:
    * `@Android`, `@iOS`: Scenario is ready for **manual** testing on these platforms.
    * `@Android_C`, `@iOS_C`: Scenario is **covered** by automation (CI/CD).
    * `@Priority_1`: **Core functionality**. Happy paths and critical edge cases. If broken, the app is unusable.
    * `@Priority_2`: **Secondary functionality**. Non-daily features or standard edge cases. App is broken but still usable.
    * `@Priority_3`: **Edge-edge cases**. Minor features or rare scenarios. App is still fully usable and functional.
5. **Given/When/Then**: Use plain English that both platforms can understand.

## Linking Preconditions
If a scenario requires a specific setup, reference it with a comment:
```gherkin
# Preconditions: @Preconditions_FeatureName_StateName
Given the app is in the specific state
...
```
Examples:

@Conventions @FF-00 @Priority_1 @Android @iOS
Scenario: FF-00 - Writing conventions - Stable IDs and clear titles
Given I am documenting a feature that already has a stable scenario ID
When I add a new scenario
Then I append it to the end of the file
And I do not renumber existing scenarios

@Conventions @FF-01 @Priority_2 @Android @iOS
Scenario: FF-01 - Writing conventions - One scenario, one behavior
Given I have one observable behavior to describe
When I write the scenario
Then I keep the title concise
And I describe only one outcome

