package org.me.awa.ui.level03

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.MainActivity
import org.me.awa.data.repository.FavoritesRepository
import org.me.awa.data.repository.WeatherRepository
import org.me.awa.ui.favorites.FavoritesScreenTags
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [34])
class AwaAppTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var favoritesRepository: FavoritesRepository

    @Inject
    lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `app starts on favorites screen and can navigate to location list and weather details`() {
        // MainActivity automatically hosts AwaApp() via setContent in its onCreate()

        // 1. Initial Screen: Favorites Screen
        composeTestRule.onNodeWithTag(FavoritesScreenTags.ADD_NEW_BUTTON).assertIsDisplayed()

        // 2. Navigate to Location List Screen
        composeTestRule.onNodeWithTag(FavoritesScreenTags.ADD_NEW_BUTTON).performClick()
        composeTestRule.onNodeWithText("Droidcon Locations").assertIsDisplayed()

        // 3. Select Berlin from the location list -> Navigates to Weather Details Screen
        composeTestRule.onNodeWithText("Berlin").performClick()
        composeTestRule.onAllNodesWithText("Berlin")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("21.5°C").assertIsDisplayed()

        // 4. Navigate Back -> Returns to Location List Screen
        composeTestRule.onNodeWithContentDescription("Navigate Back").performClick()
        composeTestRule.onNodeWithText("Droidcon Locations").assertIsDisplayed()
    }
}
