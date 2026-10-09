package org.me.awa.ui.level01

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.sampleLocations
import org.me.awa.ui.favorites.FavoriteItemUiState
import org.me.awa.ui.favorites.FavoritesScreen
import org.me.awa.ui.favorites.FavoritesScreenTags
import org.me.awa.ui.theme.AwaTheme
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import tech.dawn.template.testdsl.CallCheck
import tech.dawn.template.testdsl.dsl.assertions.hasTraversalIndex

@RunWith(RobolectricTestRunner::class)
class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `given no favorites, screen displays empty state and action buttons`() {
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

        // Node assertions: text & visibility
        composeTestRule.onNodeWithText("No Favorites Added").assertIsDisplayed()

        // Test tags & node state checks
        val addButton = composeTestRule.onNodeWithTag(FavoritesScreenTags.ADD_NEW_BUTTON)
        addButton.assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assert(hasTraversalIndex(1f))

        // Traversal index assertions for accessibility navigation
        composeTestRule.onNodeWithTag(FavoritesScreenTags.ADD_CURRENT_LOCATION_BUTTON)
            .assert(hasTraversalIndex(2f))

        addButton.performClick()
        assertTrue(addLocationCheck.wasCalled)
    }

    @Test
    fun `given favorites list, screen displays items`() {
        composeTestRule.setContent {
            AwaTheme {
                FavoritesScreen(
                    favorites = sampleLocations,
                    isLoadingLocation = false,
                    onAddLocationClick = {},
                    onAddCurrentLocationClick = {},
                    onLocationClick = {},
                    onRemoveFavorite = {},
                )
            }
        }

        // Tag assertion for list root
        composeTestRule.onNodeWithTag(FavoritesScreenTags.FAVORITES_LIST).assertIsDisplayed()

        // Node text checks
        composeTestRule.onNodeWithText("Berlin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tokyo").assertIsDisplayed()
    }

    @Test
    fun `given location loading state, add current location button is disabled`() {
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

        composeTestRule.onNodeWithTag(FavoritesScreenTags.ADD_CURRENT_LOCATION_BUTTON)
            .assertIsNotEnabled()
    }
}
