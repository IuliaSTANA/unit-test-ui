package org.me.awa

import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.domain.model.LocationData
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.domain.model.WeatherData
import org.me.awa.ui.favorites.FavoriteItemUiState

val fakeWeather = WeatherData(
    location = LocationData(latitude = 52.5200, longitude = 13.4050, cityName = "Berlin"),
    currentTemperature = 21.5,
    condition = WeatherCondition.CLEAR_SKY,
    relativeHumidity = 50,
    windSpeed = 10.0,
    hourlyForecast = emptyList()
)

val sampleLocations = listOf(
    FavoriteItemUiState(
        location = FavoriteLocation("1", "Berlin", 52.52, 13.405),
        temperature = 21.5,
        condition = WeatherCondition.CLEAR_SKY,
        isLoading = false
    ),
    FavoriteItemUiState(
        location = FavoriteLocation("2", "Tokyo", 35.676, 139.65),
        temperature = 18.0,
        condition = WeatherCondition.PARTLY_CLOUDY,
        isLoading = false
    )
)