package org.me.awa.ui.android.robot.level02

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.ui.favorites.FavoritesScreen
import org.me.awa.ui.robots.favoriteScreen
import org.me.awa.ui.theme.AwaTheme
import tech.dawn.template.testdsl.And
import tech.dawn.template.testdsl.Then
import tech.dawn.template.testdsl.annotations.Priority1
import tech.dawn.template.testdsl.annotations.Scenario
import tech.dawn.template.testdsl.dsl.assertions.displayed
import tech.dawn.template.testdsl.dsl.assertions.enabled
import tech.dawn.template.testdsl.dsl.assertions.hasCombinedText
import tech.dawn.template.testdsl.dsl.assertions.node
import tech.dawn.template.testdsl.dsl.assertions.notPresent
import tech.dawn.template.testdsl.dsl.assertions.shouldBe
import tech.dawn.template.testdsl.dsl.preconditions.precondition

@RunWith(AndroidJUnit4::class)
internal class FavoritesScreenFA01 {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     *   @Favorites @FA-01 @Priority_1 @Android
     *   Scenario: FA-01 - Empty state is displayed when no favorite locations are saved
     *     # Preconditions: @Preconditions_Favorites_EmptyState
     *     Given I have no saved favorite locations
     *     When I view the Favorites screen
     *     Then I see the empty state view with title "No Favorites Added"
     *     And I see the subtitle "Tap '+' to search and save your favorite cities."
     *     And I see the "Add Favorite Location" button enabled
     *     And I see the "Add Current Location" button enabled
     * */
    @Test
    @Scenario("FavoritesScreenFA01: Empty state is displayed when no favorite locations are saved")
    @Priority1
    fun `empty state and no favorites`() {

        precondition {
            composeTestRule.setContent {
                AwaTheme {
                    FavoritesScreen(
                        favorites = emptyList(),
                        isLoadingLocation = false,
                        onAddLocationClick = {},
                        onAddCurrentLocationClick = {},
                        onLocationClick = {},
                        onRemoveFavorite = {},
                    )
                }
            }
        }

        composeTestRule.favoriteScreen {
            Then node emptyState shouldBe displayed
            And node emptyState shouldBe hasCombinedText("No Favorites Added")
            And node addNewButton shouldBe enabled
            And node addCurrentLocationButton shouldBe enabled
            And node loadingProgress shouldBe notPresent
        }

    }
}
