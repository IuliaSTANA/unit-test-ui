package org.me.awa.ui.robots

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import tech.dawn.template.testdsl.dsl.waitUntilVisible

/**
 * Robot screen representing the Location Permission screen.
 *
 * Taking in a [ComposeTestRule], this robot encapsulates UI interactions and node accessors
 * for testing the Location Permission screen in both unit and instrumentation tests.
 */
class LocationPermissionRobot(private val rule: ComposeTestRule) {

    val title: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Location Permission & High Accuracy Rationale")

    val backButton: SemanticsNodeInteraction
        get() = rule.onNodeWithContentDescription("Navigate Back")

    val coarseStatus: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Foreground Coarse", substring = true)

    val fineStatus: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Foreground Fine", substring = true)

    val backgroundStatus: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Background Location", substring = true)

    val requestForegroundButton: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Request High Accuracy Foreground Location", substring = true)

    val upgradeHighAccuracyButton: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Upgrade to High Accuracy (Precise)")

    val requestBackgroundButton: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Request Background Location", substring = true)

    val openAppSettingsButton: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Open App Settings")

    fun clickBack(): LocationPermissionRobot {
        backButton.performClick()
        return this
    }

    fun clickRequestForeground(): LocationPermissionRobot {
        requestForegroundButton.performClick()
        return this
    }

    fun clickRequestBackground(): LocationPermissionRobot {
        requestBackgroundButton.performClick()
        return this
    }

    fun clickOpenAppSettings(): LocationPermissionRobot {
        openAppSettingsButton.performClick()
        return this
    }

    init {
        title.waitUntilVisible(rule)
    }
}
