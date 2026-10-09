package org.me.awa.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.me.awa.data.location.LocationTracker
import org.me.awa.data.repository.FavoritesRepository
import org.me.awa.data.repository.WeatherRepository
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.ui.navigation.AppNavKey
import org.me.awa.ui.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker,
    private val navigator: AppNavigator
) : ViewModel() {

    val favoritesState: StateFlow<List<FavoriteItemUiState>>
        field = MutableStateFlow<List<FavoriteItemUiState>>(emptyList())
    val isLoadingLocation: StateFlow<Boolean>
        field = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            favoritesRepository.favorites.collect { locations ->
                updateFavoritesState(locations)
            }
        }
    }

    private fun updateFavoritesState(locations: List<FavoriteLocation>) {
        val currentMap = favoritesState.value.associateBy { it.location.id }

        val newItems = locations.map { loc ->
            currentMap[loc.id] ?: FavoriteItemUiState(location = loc, isLoading = true)
        }
        favoritesState.value = newItems

        locations.forEach { loc ->
            val existing = currentMap[loc.id]
            if (existing?.temperature == null) {
                fetchWeatherForLocation(loc)
            }
        }
    }

    fun refreshAllFavorites() {
        viewModelScope.launch {
            favoritesState.value.forEach { item ->
                fetchWeatherForLocation(item.location)
            }
        }
    }

    fun fetchWeatherForLocation(location: FavoriteLocation) {
        viewModelScope.launch {
            updateSingleItemState(location.id) { it.copy(isLoading = true, error = null) }
            weatherRepository.getWeather(location.latitude, location.longitude)
                .onSuccess { weatherData ->
                    updateSingleItemState(location.id) {
                        it.copy(
                            isLoading = false,
                            temperature = weatherData.currentTemperature,
                            condition = weatherData.condition,
                            error = null
                        )
                    }
                }
                .onFailure { err ->
                    updateSingleItemState(location.id) {
                        it.copy(
                            isLoading = false,
                            error = err.localizedMessage ?: "Failed to fetch weather"
                        )
                    }
                }
        }
    }

    private fun updateSingleItemState(
        id: String,
        transform: (FavoriteItemUiState) -> FavoriteItemUiState
    ) {
        favoritesState.value = favoritesState.value.map { item ->
            if (item.location.id == id) transform(item) else item
        }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(id)
        }
    }

    fun hasLocationPermission(): Boolean = locationTracker.hasLocationPermission()

    fun onAddLocationClick() {
        navigator.navigateTo(AppNavKey.LocationList)
    }

    fun onLocationClick(location: FavoriteLocation) {
        navigator.navigateTo(
            AppNavKey.WeatherDetails(
                name = location.name,
                latitude = location.latitude,
                longitude = location.longitude
            )
        )
    }

    fun addCurrentLocationToFavorites() {
        if (!hasLocationPermission()) {
            navigator.navigateTo(AppNavKey.LocationPermission)
            return
        }

        viewModelScope.launch {
            isLoadingLocation.value = true

            val locData = locationTracker.getCurrentLocation()
            if (locData != null) {
                val currentLoc = FavoriteLocation(
                    id = "current_location",
                    name = "Current Location",
                    latitude = locData.latitude,
                    longitude = locData.longitude,
                    isCurrentLocation = true
                )
                favoritesRepository.addFavorite(currentLoc)
            } else {
                val currentLoc = FavoriteLocation(
                    id = "current_location",
                    name = "Current Location",
                    latitude = 52.5200,
                    longitude = 13.4050,
                    isCurrentLocation = true
                )
                favoritesRepository.addFavorite(currentLoc)
            }
            isLoadingLocation.value = false
        }
    }
}
