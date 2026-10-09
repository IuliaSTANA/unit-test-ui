package org.me.awa.ui.robot.level02

import androidx.compose.ui.test.junit4.v2.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.ui.favorites.FavoriteItemUiState
import org.me.awa.ui.favorites.FavoritesScreen
import org.me.awa.ui.robots.favoriteScreen
import org.me.awa.ui.theme.AwaTheme
import org.robolectric.RobolectricTestRunner
import tech.dawn.template.testdsl.And
import tech.dawn.template.testdsl.Then
import tech.dawn.template.testdsl.annotations.Priority1
import tech.dawn.template.testdsl.annotations.Scenario
import tech.dawn.template.testdsl.dsl.assertions.displayed
import tech.dawn.template.testdsl.dsl.assertions.hasCombinedText
import tech.dawn.template.testdsl.dsl.assertions.node
import tech.dawn.template.testdsl.dsl.assertions.notPresent
import tech.dawn.template.testdsl.dsl.assertions.shouldBe
import tech.dawn.template.testdsl.dsl.preconditions.precondition

@RunWith(RobolectricTestRunner::class)
internal class FavoritesScreenFA02 {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     *   @Favorites @FA-02 @Priority_1 @Android
     *   Scenario: FA-02 - List displays favorite locations with updated weather data
     *     # Preconditions: @Preconditions_Favorites_WithFavorites
     *     # Data: Data/Favorites/favorite-locations.json
     *     # Data: Data/Favorites/berlin-weather-success.json
     *     Given I have saved favorite locations including "Berlin"
     *     And weather data is successfully loaded for "Berlin"
     *     When I view the Favorites screen
     *     Then I see "Berlin" in the list of favorite locations
     *     And I see the current temperature "21.5°C" displayed for "Berlin"
     *     And I see the weather condition description "Clear sky" with its icon
     * */
    @Test
    @Scenario("FavoritesScreenFA02: List displays favorite locations with updated weather data")
    @Priority1
    fun `list displays favorite locations with weather data`() {
        val berlinState = FavoriteItemUiState(
            location = FavoriteLocation("1", "Berlin", 52.52, 13.405),
            temperature = 21.5,
            condition = WeatherCondition.CLEAR_SKY,
            isLoading = false
        )

        precondition {
            composeTestRule.setContent {
                AwaTheme {
                    FavoritesScreen(
                        favorites = listOf(berlinState),
                        isLoadingLocation = false,
                        onAddLocationClick = {},
                        onAddCurrentLocationClick = {},
                        onLocationClick = {},
                        onRemoveFavorite = {},
                    )
                }
            }
        }

        composeTestRule.favoriteScreen {
            Then node favoritesList shouldBe displayed
            And node favoriteItem("1") shouldBe displayed
            And node favoriteItem("1") shouldBe hasCombinedText("Berlin", "21.5°C", "Clear sky")
            And node emptyState shouldBe notPresent
        }
    }
}
