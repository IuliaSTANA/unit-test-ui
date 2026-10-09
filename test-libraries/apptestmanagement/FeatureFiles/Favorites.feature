Feature: Favorites Screen Behavior

  As a user of the weather app
  I want to view and manage my saved favorite locations and their current weather
  So that I can quickly check weather updates for locations I care about

  @Favorites @FA-01 @Priority_1 @Android
  Scenario: FA-01 - Empty state is displayed when no favorite locations are saved
    # Preconditions: @Preconditions_Favorites_EmptyState
    Given I have no saved favorite locations
    When I view the Favorites screen
    Then I see the empty state view with title "No Favorites Added"
    And I see the "Add Favorite Location" button enabled
    And I see the "Add Current Location" button enabled

  @Favorites @FA-02 @Priority_1 @Android
  Scenario: FA-02 - List displays favorite locations with updated weather data
    # Preconditions: @Preconditions_Favorites_WithFavorites
    # Data: Data/Favorites/favorite-locations.json
    # Data: Data/Favorites/berlin-weather-success.json
    Given I have saved favorite locations including "Berlin"
    And weather data is successfully loaded for "Berlin"
    When I view the Favorites screen
    Then I see "Berlin" in the list of favorite locations
    And I see the current temperature "21.5°C" displayed for "Berlin"
    And I see the weather condition description "Clear sky" with its icon

  @Favorites @FA-03 @Priority_2 @Android
  Scenario: FA-03 - Favorite location card shows inline loading indicator while fetching weather
    # Preconditions: @Preconditions_Favorites_FetchingWeather
    # Data: Data/Favorites/favorite-locations.json
    Given I have saved favorite locations including "London"
    And weather data is currently being fetched for "London"
    When I view the Favorites screen
    Then I see "London" in the list of favorite locations
    And I see a circular loading indicator on the "London" card
    And I see the loading message "Fetching weather data..."

  @Favorites @FA-04 @Priority_2 @Android
  Scenario: FA-04 - Favorite location card shows error message when weather fetch fails
    # Preconditions: @Preconditions_Favorites_WeatherError
    # Data: Data/Favorites/favorite-locations.json
    # Data: Data/Favorites/weather-error-response.json
    Given I have saved favorite locations including "Tokyo"
    And weather data request for "Tokyo" fails
    When I view the Favorites screen
    Then I see "Tokyo" in the list of favorite locations
    And I see the error message "Failed to fetch weather" displayed on the "Tokyo" card

  @Favorites @FA-05 @Priority_2 @Android
  Scenario: FA-05 - Adding current location displays progress bar and disables current location button
    # Preconditions: @Preconditions_Favorites_FetchingLocation
    Given the app is resolving the current device location
    When I view the Favorites screen
    Then I see a linear progress indicator at the top of the content area
    And the "Add Current Location" button is disabled

  @Favorites @FA-06 @Priority_1 @Android
  Scenario: FA-06 - Tapping Add Favorite Location navigates to location search screen
    # Preconditions: @Preconditions_Favorites_EmptyState
    Given I am viewing the Favorites screen
    When I tap the "Add Favorite Location" button
    Then I am navigated to the location search screen

  @Favorites @FA-07 @Priority_1 @Android
  Scenario: FA-07 - Tapping Add Current Location prompts for location permission if missing
    # Preconditions: @Preconditions_Favorites_NoLocationPermission
    Given location permissions are not granted to the app
    And I am viewing the Favorites screen
    When I tap the "Add Current Location" button
    Then I am navigated to the location permission screen

  @Favorites @FA-08 @Priority_1 @Android
  Scenario: FA-08 - Removing a favorite location updates the list
    # Preconditions: @Preconditions_Favorites_WithFavorites
    # Data: Data/Favorites/favorite-locations.json
    Given I have saved "Berlin" in my favorite locations
    And I am viewing the Favorites screen
    When I tap the delete icon button for "Berlin"
    Then "Berlin" is removed from my favorite locations
    And "Berlin" is no longer displayed on the screen

  @Favorites @FA-09 @Priority_1 @Android
  Scenario: FA-09 - Tapping a favorite location card opens weather details screen
    # Preconditions: @Preconditions_Favorites_WithFavorites
    # Data: Data/Favorites/favorite-locations.json
    Given I have saved "Berlin" in my favorite locations
    And I am viewing the Favorites screen
    When I tap the card for "Berlin"
    Then I am navigated to the weather details screen for "Berlin"

  @Favorites @FA-10 @Priority_2 @Android
  Scenario: FA-10 - Weather data is refreshed when the screen resumes
    # Preconditions: @Preconditions_Favorites_WithFavorites
    # Data: Data/Favorites/favorite-locations.json
    Given I am viewing the Favorites screen with saved locations
    When the screen transitions from background to foreground state
    Then the app requests refreshed weather data for all favorite locations
