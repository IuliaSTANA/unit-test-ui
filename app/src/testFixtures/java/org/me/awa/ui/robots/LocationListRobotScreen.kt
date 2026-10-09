package org.me.awa.ui.robots

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import tech.dawn.template.testdsl.dsl.waitUntilVisible

/**
 * Robot screen representing the Location List screen.
 *
 * Encapsulates UI elements and interactions for the Droidcon Locations list screen.
 */
class LocationListRobotScreen(private val rule: ComposeTestRule) {

    val title: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Droidcon Locations")

    val backButton: SemanticsNodeInteraction
        get() = rule.onNodeWithContentDescription("Navigate Back")

    fun locationItem(name: String): SemanticsNodeInteraction =
        rule.onNodeWithText(name)

    fun clickLocation(name: String): LocationListRobotScreen {
        locationItem(name).performClick()
        return this
    }

    fun clickBack(): LocationListRobotScreen {
        backButton.performClick()
        return this
    }

    init {
        title.waitUntilVisible(rule)
    }
}

inline fun ComposeTestRule.locationListScreen(f: LocationListRobotScreen.() -> Unit) =
    f(LocationListRobotScreen(this))
