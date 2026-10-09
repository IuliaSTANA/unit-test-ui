package org.me.awa.ui.level01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.domain.model.LocationData
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.domain.model.WeatherData
import org.me.awa.fakeWeather
import org.me.awa.ui.theme.AwaTheme
import org.me.awa.ui.weather.WeatherScreen
import org.me.awa.ui.weather.WeatherScreenTags
import org.me.awa.ui.weather.WeatherUiState
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.dawn.template.testdsl.CallCheck

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class WeatherScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherScreen_displaysTemperatureAndCity() {
        val toggleFavoriteCheck = CallCheck()
        composeTestRule.setContent {
            AwaTheme {
                WeatherScreen(
                    locationName = "Berlin",
                    uiState = WeatherUiState.Success(fakeWeather),
                    isFavorite = true,
                    onRefresh = {},
                    onToggleFavorite = toggleFavoriteCheck::invoke,
                    onBackClick = {}
                )
            }
        }

        composeTestRule.onAllNodesWithText("Berlin")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("21.5°C").assertIsDisplayed()
        val toggleFavoriteButton = composeTestRule.onNodeWithTag(WeatherScreenTags.TOGGLE_FAVORITE)
        toggleFavoriteButton.performClick()

        assertTrue(toggleFavoriteCheck.wasCalled)
    }
}
