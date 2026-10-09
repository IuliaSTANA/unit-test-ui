Feature: Core User Flows and App Navigation

  As a user opening the AWA Weather App
  I want to navigate between my favorites, the location list, and weather details
  So that I can easily discover and view weather forecasts for different cities

  @CoreFlows @CF-01 @Priority_1 @Android
  Scenario: CF-01 - User navigates from favorites to location list, views weather details, and navigates back
    # Preconditions: @Preconditions_CoreFlows_DefaultState
    Given I am on the main Favorites screen
    When I tap the "Add New Location" button
    Then I see the location list screen titled "Droidcon Locations"
    When I select "Berlin" from the location list
    Then I see the weather details screen for "Berlin" displaying temperature "21.5°C"
    When I tap the back navigation button
    Then I am returned to the "Droidcon Locations" list screen

  @CoreFlows @CF-02 @Priority_1 @Android
  Scenario: CF-02 - Opening a web deep link routes directly to weather details screen
    # Preconditions: @Preconditions_CoreFlows_DefaultState
    Given a user opens a web deep link URL "https://org.me.awa/weather?name=Berlin&lat=52.52&lon=13.405"
    When the app processes the deep link intent
    Then the app directly displays the weather details screen for "Berlin" with temperature "21.5°C"
