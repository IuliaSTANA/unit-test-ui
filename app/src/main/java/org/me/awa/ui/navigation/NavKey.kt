package org.me.awa.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppNavKey : NavKey {
    @Serializable
    data object Favorites : AppNavKey

    @Serializable
    data object LocationList : AppNavKey

    @Serializable
    data class WeatherDetails(
        val name: String,
        val latitude: Double,
        val longitude: Double
    ) : AppNavKey

    @Serializable
    data object LocationPermission : AppNavKey
}
