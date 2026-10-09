package org.me.awa.ui.level03

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.MainActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class DeepLinkNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `launching deep link intent opens Weather Details screen for Berlin`() {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://org.me.awa/weather?name=Berlin&lat=52.52&lon=13.405")
        ).apply {
            setPackage(ApplicationProvider.getApplicationContext<Context>().packageName)
        }
        ActivityScenario.launch<MainActivity>(deepLinkIntent).use {
            composeTestRule.onAllNodesWithText("Berlin")[0].assertIsDisplayed()
            composeTestRule.onNodeWithText("21.5°C").assertIsDisplayed()
        }
    }

    @Test
    fun `launching custom scheme deep link intent opens Weather Details screen for Tokyo`() {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("awa://weather?name=Tokyo&lat=35.6762&lon=139.6503")
        ).apply {
            setPackage(ApplicationProvider.getApplicationContext<Context>().packageName)
        }

        ActivityScenario.launch<MainActivity>(deepLinkIntent).use {
            composeTestRule.onAllNodesWithText("Tokyo")[0].assertIsDisplayed()
            composeTestRule.onNodeWithText("21.5°C").assertIsDisplayed()
        }
    }
}
