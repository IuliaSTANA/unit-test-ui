package org.me.awa.ui.level02

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.me.awa.data.location.FakeLocationTracker
import org.me.awa.data.repository.FakeFavoritesRepository
import org.me.awa.data.repository.FakeWeatherRepository
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.fakeWeather
import org.me.awa.ui.favorites.FavoritesRoute
import org.me.awa.ui.favorites.FavoritesViewModel
import org.me.awa.ui.navigation.AppNavigator
import org.me.awa.ui.theme.AwaTheme
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

private class TestLifecycleOwner(
    initialState: Lifecycle.State = Lifecycle.State.INITIALIZED
) : LifecycleOwner {
    override val lifecycle: Lifecycle
        field = LifecycleRegistry(this).apply {
            currentState = initialState
        }

    fun handleLifecycleEvent(event: Lifecycle.Event) {
        lifecycle.handleLifecycleEvent(event)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FavoritesRouteTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeFavoritesRepo = FakeFavoritesRepository()
    private val fakeWeatherRepo = FakeWeatherRepository()
    private val fakeLocationTracker = FakeLocationTracker()
    private val navigator = AppNavigator()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when favorite location is removed, route performs state transition and updates UI`() = runTest {
        // Given: fake repository starts with Berlin as a favorite location
        val berlin = FavoriteLocation(id = "1", name = "Berlin", latitude = 52.52, longitude = 13.405)
        fakeFavoritesRepo.addFavorite(berlin)

        val viewModel = FavoritesViewModel(
            favoritesRepository = fakeFavoritesRepo,
            weatherRepository = fakeWeatherRepo,
            locationTracker = fakeLocationTracker,
            navigator = navigator
        )

        composeTestRule.setContent {
            AwaTheme {
                FavoritesRoute(viewModel = viewModel)
            }
        }

        // Verify initial state: Berlin card is rendered
        composeTestRule.onNodeWithText("Berlin").assertIsDisplayed()

        // When: User clicks the remove favorite button on the card
        composeTestRule.onNodeWithContentDescription("Remove Favorite").performClick()

        // Then: State transition completes — Berlin card disappears and empty state is shown
        composeTestRule.onNodeWithText("Berlin").assertDoesNotExist()
        composeTestRule.onNodeWithText("No Favorites Added").assertIsDisplayed()
    }

    @Test
    fun `when screen lifecycle transitions to RESUMED, weather data is refreshed`() = runTest {
        // Given: favorite location Berlin is present
        val berlin = FavoriteLocation(id = "1", name = "Berlin", latitude = 52.52, longitude = 13.405)
        fakeFavoritesRepo.addFavorite(berlin)

        val viewModel = FavoritesViewModel(
            favoritesRepository = fakeFavoritesRepo,
            weatherRepository = fakeWeatherRepo,
            locationTracker = fakeLocationTracker,
            navigator = navigator
        )

        val lifecycleOwner = TestLifecycleOwner(initialState = Lifecycle.State.INITIALIZED)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                AwaTheme {
                    FavoritesRoute(viewModel = viewModel)
                }
            }
        }

        // 1. Initial lifecycle transition to ON_RESUME
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // Verify initial temperature 21.5°C is displayed and fetch count is recorded
        composeTestRule.onNodeWithText("21.5°C").assertIsDisplayed()
        val initialFetchCount = fakeWeatherRepo.fetchCount
        assertTrue("Expected initial fetch count to be at least 1", initialFetchCount >= 1)

        // 2. Update fake weather repository with new weather data (28.0°C)
        fakeWeatherRepo.weatherResult = Result.success(fakeWeather.copy(currentTemperature = 28.0))

        // 3. Simulate app moving to background (ON_PAUSE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)

        // Verify temperature in UI remains 21.5°C while paused and fetch count is unchanged
        composeTestRule.onNodeWithText("21.5°C").assertIsDisplayed()
        assertEquals(initialFetchCount, fakeWeatherRepo.fetchCount)

        // 4. Simulate app returning to foreground (ON_RESUME)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        // 5. Verify that ON_RESUME triggered refresh: 28.0°C is displayed & fetchCount incremented by 1
        composeTestRule.onNodeWithText("28.0°C").assertIsDisplayed()
        assertEquals(initialFetchCount + 1, fakeWeatherRepo.fetchCount)
    }
}