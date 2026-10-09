package org.me.awa.ui.robot.level03

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.MainActivity
import org.me.awa.ui.robots.favoriteScreen
import org.me.awa.ui.robots.locationListScreen
import org.me.awa.ui.robots.weatherScreen
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.dawn.template.testdsl.And
import tech.dawn.template.testdsl.Then
import tech.dawn.template.testdsl.When
import tech.dawn.template.testdsl.annotations.Priority1
import tech.dawn.template.testdsl.annotations.Scenario
import tech.dawn.template.testdsl.dsl.actions.press
import tech.dawn.template.testdsl.dsl.assertions.displayed
import tech.dawn.template.testdsl.dsl.assertions.node
import tech.dawn.template.testdsl.dsl.assertions.shouldBe
import tech.dawn.template.testdsl.dsl.preconditions.precondition

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [34])
internal class CoreFlowsCF01 {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    /**
     *   @CoreFlows @CF-01 @Priority_1 @Android
     *   Scenario: CF-01 - User navigates from favorites to location list, views weather details, and navigates back
     *     # Preconditions: @Preconditions_CoreFlows_DefaultState
     *     Given I am on the main Favorites screen
     *     When I tap the "Add New Location" button
     *     Then I see the location list screen titled "Droidcon Locations"
     *     When I select "Berlin" from the location list
     *     Then I see the weather details screen for "Berlin" displaying temperature "21.5°C"
     *     When I tap the back navigation button
     *     Then I am returned to the "Droidcon Locations" list screen
     * */
    @Test
    @Scenario("CoreFlowsCF01: User navigates from favorites to location list, views weather details, and navigates back")
    @Priority1
    fun `navigate from favorites to location list, weather details and back`() {

        precondition {
            // MainActivity automatically hosts AwaApp() via setContent in its onCreate()
        }

        // 1. Initial Screen: Favorites Screen
        composeTestRule.favoriteScreen {
            Then node addNewButton shouldBe displayed
            When press addNewButton
        }

        // 2. Navigate to Location List Screen
        composeTestRule.locationListScreen {
            Then node title shouldBe displayed
            When press locationItem("Berlin")
        }

        // 3. Select Berlin -> Weather Details Screen
        composeTestRule.weatherScreen {
            Then node locationName("Berlin") shouldBe displayed
            And node temperature("21.5°C") shouldBe displayed
            When press backButton
        }

        // 4. Navigate Back -> Returned to Location List Screen
        composeTestRule.locationListScreen {
            Then node title shouldBe displayed
        }
    }
}
