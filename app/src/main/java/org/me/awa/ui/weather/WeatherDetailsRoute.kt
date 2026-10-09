package org.me.awa.ui.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.me.awa.ui.navigation.AppNavKey

@Composable
fun WeatherDetailsRoute(
    key: AppNavKey.WeatherDetails,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(key) {
        viewModel.loadWeatherForLocation(
            name = key.name,
            latitude = key.latitude,
            longitude = key.longitude
        )
    }

    WeatherScreen(
        locationName = key.name,
        uiState = uiState,
        isFavorite = isFavorite,
        onRefresh = viewModel::refreshWeather,
        onToggleFavorite = viewModel::toggleFavorite,
        onBackClick = viewModel::onBackClick,
        modifier = modifier
    )
}
