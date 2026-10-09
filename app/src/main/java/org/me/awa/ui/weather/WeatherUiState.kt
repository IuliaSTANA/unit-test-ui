package org.me.awa.ui.weather

import org.me.awa.domain.model.WeatherData

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data object PermissionRequired : WeatherUiState
    data class Success(val weather: WeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}
