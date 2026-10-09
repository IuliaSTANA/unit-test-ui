package org.me.awa.ui.favorites

import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.domain.model.WeatherCondition

data class FavoriteItemUiState(
    val location: FavoriteLocation,
    val temperature: Double? = null,
    val condition: WeatherCondition? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
