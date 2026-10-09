package org.me.awa.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.me.awa.data.repository.FavoritesRepository
import org.me.awa.data.repository.WeatherRepository
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.domain.model.LocationData
import org.me.awa.ui.navigation.AppNavKey
import org.me.awa.ui.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val favoritesRepository: FavoritesRepository,
    private val navigator: AppNavigator
) : ViewModel() {

    val uiState: StateFlow<WeatherUiState>
        field = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)

    val isFavorite: StateFlow<Boolean>
        field = MutableStateFlow(false)

    private var currentLocationName: String = ""
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0

    fun loadWeatherForLocation(name: String, latitude: Double, longitude: Double) {
        currentLocationName = name
        currentLatitude = latitude
        currentLongitude = longitude

        viewModelScope.launch {
            favoritesRepository.favorites.collect {
                isFavorite.value = favoritesRepository.isFavorite(latitude, longitude)
            }
        }

        refreshWeather()
    }

    fun refreshWeather() {
        viewModelScope.launch {
            uiState.value = WeatherUiState.Loading
            val loc = LocationData(latitude = currentLatitude, longitude = currentLongitude, cityName = currentLocationName)
            weatherRepository.getWeather(currentLatitude, currentLongitude)
                .onSuccess { weatherData ->
                    uiState.value = WeatherUiState.Success(
                        weatherData.copy(location = loc)
                    )
                }
                .onFailure { error ->
                    uiState.value = WeatherUiState.Error(
                        error.localizedMessage ?: "Failed to load weather data"
                    )
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (isFavorite.value) {
                favoritesRepository.removeFavoriteByCoordinates(currentLatitude, currentLongitude)
            } else {
                val fav = FavoriteLocation(
                    id = "${currentLatitude}_${currentLongitude}",
                    name = currentLocationName,
                    latitude = currentLatitude,
                    longitude = currentLongitude
                )
                favoritesRepository.addFavorite(fav)
            }
            navigator.cleanAndNavigateTo(AppNavKey.Favorites)
        }
    }

    fun onBackClick() {
        navigator.navigateBack()
    }
}
