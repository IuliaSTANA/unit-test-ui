package org.me.awa.ui.robots

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.me.awa.ui.favorites.FavoritesScreenTags

/**
 * Robot screen representing the Favorites screen.
 *
 * Taking in a [ComposeTestRule], this robot encapsulates all relevant UI elements
 * and interactions. If UI details change (e.g. test tags, layout structure, or text),
 * those changes are contained within this robot screen definition, rather than requiring
 * modifications across all tests.
 */
class FavoriteRobotScreen(private val rule: ComposeTestRule) {

    val root: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.ROOT)

    val title: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.TITLE)

    val addNewButton: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.ADD_NEW_BUTTON)

    val addCurrentLocationButton: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.ADD_CURRENT_LOCATION_BUTTON)

    val favoritesList: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.FAVORITES_LIST)

    /**
     * Optional linear progress indicator field displayed when loading location.
     */
    val loadingProgress: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.LOADING_PROGRESS)

    /**
     * Empty state container using [mergeDescendants = true].
     * Merges child text elements (emoji, title, subtitle) into a single semantic tree node.
     */
    val emptyState: SemanticsNodeInteraction
        get() = rule.onNodeWithTag(FavoritesScreenTags.EMPTY_STATE)

    /**
     * Header for favorite locations list, located by user-visible text instead of test tag.
     * Demonstrates finding a node by text while encapsulating the search strategy inside the robot.
     */
    val favoritesHeader: SemanticsNodeInteraction
        get() = rule.onNodeWithText("Favorite Locations")

    init {
        rule.waitUntil(3_000) {
            try {
                root.assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }

    /**
     * Retrieves a favorite item card by its unique location [id].
     * Highlights that each list item needs a unique identifier for deterministic test interactions.
     */
    fun favoriteItem(id: String): SemanticsNodeInteraction =
        rule.onNodeWithTag(FavoritesScreenTags.favoriteItemTag(id))

    /**
     * Retrieves the delete button for a specific favorite item by location [id].
     */
    fun favoriteItemDeleteButton(id: String): SemanticsNodeInteraction =
        rule.onNodeWithTag(FavoritesScreenTags.favoriteItemDeleteTag(id))

    /**
     * Clicks the "Add New Location" button.
     */
    fun clickAddNewLocation(): FavoriteRobotScreen {
        addNewButton.performClick()
        return this
    }

    /**
     * Clicks the "Add Current Location" button.
     */
    fun clickAddCurrentLocation(): FavoriteRobotScreen {
        addCurrentLocationButton.performClick()
        return this
    }

    /**
     * Clicks the delete button for a favorite item by location [id].
     */
    fun clickDeleteFavorite(id: String): FavoriteRobotScreen {
        favoriteItemDeleteButton(id).performClick()
        return this
    }
}

inline fun ComposeContentTestRule.favoriteScreen(f: FavoriteRobotScreen.() -> Unit) =
    f(FavoriteRobotScreen(this))