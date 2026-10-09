package org.me.awa.ui.robot.level03

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
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
import org.me.awa.ui.robots.weatherScreen
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.dawn.template.testdsl.And
import tech.dawn.template.testdsl.Then
import tech.dawn.template.testdsl.annotations.Priority1
import tech.dawn.template.testdsl.annotations.Scenario
import tech.dawn.template.testdsl.dsl.assertions.displayed
import tech.dawn.template.testdsl.dsl.assertions.node
import tech.dawn.template.testdsl.dsl.assertions.shouldBe
import tech.dawn.template.testdsl.dsl.preconditions.precondition

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [34])
internal class CoreFlowsCF02 {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    /**
     *   @CoreFlows @CF-02 @Priority_1 @Android
     *   Scenario: CF-02 - Opening a web deep link routes directly to weather details screen
     *     # Preconditions: @Preconditions_CoreFlows_DefaultState
     *     Given a user opens a web deep link URL "https://org.me.awa/weather?name=Berlin&lat=52.52&lon=13.405"
     *     When the app processes the deep link intent
     *     Then the app directly displays the weather details screen for "Berlin" with temperature "21.5°C"
     * */
    @Test
    @Scenario("CoreFlowsCF02: Opening a web deep link routes directly to weather details screen")
    @Priority1
    fun `opening web deep link opens weather details screen`() {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://org.me.awa/weather?name=Berlin&lat=52.52&lon=13.405")
        ).apply {
            setClassName(
                ApplicationProvider.getApplicationContext<HiltTestApplication>(),
                MainActivity::class.java.name
            )
        }

        precondition {
            // Intent set up with target web deep link
        }

        ActivityScenario.launch<MainActivity>(deepLinkIntent).use {
            composeTestRule.weatherScreen {
                Then node locationName("Berlin") shouldBe displayed
                And node temperature("21.5°C") shouldBe displayed
            }
        }
    }
}
