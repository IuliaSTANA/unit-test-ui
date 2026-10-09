package org.me.awa.domain.model

data class HourlyForecastData(
    val time: String,
    val temperature: Double,
    val condition: WeatherCondition
)

data class WeatherData(
    val location: LocationData,
    val currentTemperature: Double,
    val condition: WeatherCondition,
    val relativeHumidity: Int?,
    val windSpeed: Double?,
    val hourlyForecast: List<HourlyForecastData>
)
