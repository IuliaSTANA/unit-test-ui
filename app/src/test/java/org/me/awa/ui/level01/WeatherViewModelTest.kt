package org.me.awa.ui.level01

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
import org.junit.Test
import org.me.awa.data.repository.FakeFavoritesRepository
import org.me.awa.data.repository.FakeWeatherRepository
import org.me.awa.data.repository.WeatherRepository
import org.me.awa.domain.model.WeatherData
import org.me.awa.fakeWeather
import org.me.awa.ui.navigation.AppNavKey
import org.me.awa.ui.navigation.AppNavigator
import org.me.awa.ui.weather.WeatherUiState
import org.me.awa.ui.weather.WeatherViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepository = FakeWeatherRepository()
    private val fakeFavoritesRepository = FakeFavoritesRepository()
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
    fun `when loadWeatherForLocation is called, state emits Success`() = runTest {
        val viewModel = WeatherViewModel(fakeRepository, fakeFavoritesRepository, navigator)
        viewModel.loadWeatherForLocation("Berlin", 52.5200, 13.4050)

        val currentState = viewModel.uiState.value
        assertTrue(currentState is WeatherUiState.Success)
        val successState = currentState as WeatherUiState.Success
        assertEquals(21.5, successState.weather.currentTemperature, 0.01)
    }

    @Test
    fun `when onBackClick is called, navigator navigates back`() = runTest {
        navigator.navigateTo(AppNavKey.WeatherDetails("Berlin", 52.5200, 13.4050))
        val viewModel = WeatherViewModel(fakeRepository, fakeFavoritesRepository, navigator)

        viewModel.onBackClick()

        assertEquals(listOf(AppNavKey.Favorites), navigator.backStack.toList())
    }
}
