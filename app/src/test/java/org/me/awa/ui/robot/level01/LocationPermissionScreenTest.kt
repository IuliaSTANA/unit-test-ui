package org.me.awa.ui.robot.level01

import android.Manifest
import android.app.Application
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.ui.permission.LocationPermissionScreen
import org.me.awa.ui.robots.LocationPermissionRobot
import org.me.awa.ui.theme.AwaTheme
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import tech.dawn.template.testdsl.dsl.android.PermissionPromptRegistry

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LocationPermissionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given permissions granted via Robolectric shadow, robot displays granted status`() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val shadowApp = shadowOf(application)

        shadowApp.grantPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        composeTestRule.setContent {
            AwaTheme {
                LocationPermissionScreen(onBackClick = {})
            }
        }

        val robot = LocationPermissionRobot(composeTestRule)

        robot.title.assertIsDisplayed()
        composeTestRule.onNodeWithText("• Foreground Coarse (Approximate): GRANTED")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("• Foreground Fine (Precise / High Accuracy): GRANTED")
            .assertIsDisplayed()
    }

    @Test
    fun `given permission request triggered, robot triggers launcher and handles registry response`() {
        val registry = PermissionPromptRegistry()

        composeTestRule.setContent {
            CompositionLocalProvider(LocalActivityResultRegistryOwner provides registry) {
                AwaTheme {
                    LocationPermissionScreen(onBackClick = {})
                }
            }
        }

        val robot = LocationPermissionRobot(composeTestRule)

        // Before clicking, no pending requests
        assertFalse(registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION))

        // Trigger request using Robot method
        robot.clickRequestForeground()

        // Verify PermissionPromptRegistry captured the launch request
        assertTrue(registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION))
        assertTrue(registry.hasPending(Manifest.permission.ACCESS_COARSE_LOCATION))

        // Simulate user granting permission in system dialog via registry response
        registry.respondTo(Manifest.permission.ACCESS_FINE_LOCATION, true)

        // Verify requests are no longer pending after response
        assertFalse(registry.hasPending(Manifest.permission.ACCESS_FINE_LOCATION))
        assertFalse(registry.hasPending(Manifest.permission.ACCESS_COARSE_LOCATION))

        composeTestRule.onNodeWithText("• Foreground Coarse (Approximate): GRANTED")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("• Foreground Fine (Precise / High Accuracy): GRANTED")
            .assertIsDisplayed()
    }
}
