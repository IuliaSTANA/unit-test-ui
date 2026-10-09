package org.me.awa.ui.robot.level01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.v2.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.sampleLocations
import org.me.awa.ui.favorites.FavoritesScreen
import org.me.awa.ui.robots.FavoriteRobotScreen
import org.me.awa.ui.theme.AwaTheme
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.dawn.template.testdsl.CallCheck

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given no favorites, empty state is shown`() {
        val addLocationCheck = CallCheck()

        composeTestRule.setContent {
            AwaTheme {
                FavoritesScreen(
                    favorites = emptyList(),
                    isLoadingLocation = false,
                    onAddLocationClick = addLocationCheck::invoke,
                    onAddCurrentLocationClick = {},
                    onLocationClick = {},
                    onRemoveFavorite = {},
                )
            }
        }

        val robot = FavoriteRobotScreen(composeTestRule)

        // Merged empty state node contains title and subtitle children
        robot.emptyState.assertIsDisplayed()
            .assertTextContains("No Favorites Added")

        // Linear progress indicator should not exist when not loading
        robot.loadingProgress.assertDoesNotExist()

        // High-level user interaction using robot method
        robot.clickAddNewLocation()
        assertTrue(addLocationCheck.wasCalled)
    }

    @Test
    fun `given a favorites list, the screen shows the list`() {
        var removedId: String? = null

        composeTestRule.setContent {
            AwaTheme {
                FavoritesScreen(
                    favorites = sampleLocations,
                    isLoadingLocation = false,
                    onAddLocationClick = {},
                    onAddCurrentLocationClick = {},
                    onLocationClick = {},
                    onRemoveFavorite = { removedId = it },
                )
            }
        }

        val robot = FavoriteRobotScreen(composeTestRule)

        // Header node found by user-visible text rather than test tag
        robot.favoritesHeader.assertIsDisplayed()

        // List items targeted deterministically by unique location id
        robot.favoriteItem("1").assertIsDisplayed().assertTextContains("Berlin")
        robot.favoriteItem("2").assertIsDisplayed().assertTextContains("Tokyo")

        // Interacting with delete action using item's unique id
        robot.clickDeleteFavorite("1")
        assertEquals("1", removedId)
    }

    @Test
    fun `given location loading state, linear progress indicator is displayed`() {
        composeTestRule.setContent {
            AwaTheme {
                FavoritesScreen(
                    favorites = emptyList(),
                    isLoadingLocation = true,
                    onAddLocationClick = {},
                    onAddCurrentLocationClick = {},
                    onLocationClick = {},
                    onRemoveFavorite = {},
                )
            }
        }

        val robot = FavoriteRobotScreen(composeTestRule)

        // Linear progress field is displayed during location loading
        robot.loadingProgress.assertIsDisplayed()

        // Add current location button is disabled during loading
        robot.addCurrentLocationButton.assertIsNotEnabled()
    }
}
