package org.me.awa.ui.android.robot.level01

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.sampleLocations
import org.me.awa.ui.favorites.FavoritesScreen
import org.me.awa.ui.robots.FavoriteRobotScreen
import org.me.awa.ui.theme.AwaTheme
import tech.dawn.template.testdsl.CallCheck

@RunWith(AndroidJUnit4::class)
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenNoFavorites_emptyStateIsShown() {
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
    fun givenFavoritesList_screenShowsList() {
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
    fun givenLocationLoadingState_linearProgressIndicatorIsDisplayed() {
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
