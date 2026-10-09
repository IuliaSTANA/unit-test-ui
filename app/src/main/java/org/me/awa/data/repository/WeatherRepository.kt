package org.me.awa.data.repository

import org.me.awa.domain.model.WeatherData

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherData>
}
