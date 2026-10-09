Feature: Feature file writing conventions

# Copy this file when starting a new feature file.
# Keep IDs stable and add new scenarios at the end.
# Use one observable behavior per scenario.

@Conventions @FF-00 @Priority_3 @Android @iOS
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

