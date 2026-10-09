package org.me.awa.ui.robots

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.me.awa.ui.weather.WeatherScreenTags
import tech.dawn.template.testdsl.dsl.waitUntilVisible

/**
 * Robot screen representing the Weather Details screen.
 *
 * Encapsulates UI elements and interactions for the Weather Details screen.
 */
class WeatherRobotScreen(private val rule: ComposeTestRule) {

    val backButton: SemanticsNodeInteraction
        get() = rule.onNodeWithContentDescription("Navigate Back")

    val toggleFavoriteButton: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(WeatherScreenTags.TOGGLE_FAVORITE)

    val refreshButton: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Refresh Weather")

    fun locationName(name: String): SemanticsNodeInteraction =
        rule.onAllNodesWithText(name)[0]

    fun temperature(tempText: String): SemanticsNodeInteraction =
        rule.onNodeWithText(tempText)

    fun clickBack(): WeatherRobotScreen {
        backButton.performClick()
        return this
    }

    fun clickToggleFavorite(): WeatherRobotScreen {
        toggleFavoriteButton.performClick()
        return this
    }

    fun clickRefresh(): WeatherRobotScreen {
        refreshButton.performClick()
        return this
    }

    init {
        backButton.waitUntilVisible(rule)
    }
}

inline fun ComposeTestRule.weatherScreen(f: WeatherRobotScreen.() -> Unit) =
    f(WeatherRobotScreen(this))
