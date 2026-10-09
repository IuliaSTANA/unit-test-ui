package org.me.awa.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeatherData? = null,
    val hourly: HourlyWeatherData? = null
)

@Serializable
data class CurrentWeatherData(
    val time: String,
    @SerialName("temperature_2m")
    val temperature: Double,
    @SerialName("relative_humidity_2m")
    val relativeHumidity: Int? = null,
    @SerialName("weather_code")
    val weatherCode: Int,
    @SerialName("wind_speed_10m")
    val windSpeed: Double? = null
)

@Serializable
data class HourlyWeatherData(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m")
    val temperature: List<Double> = emptyList(),
    @SerialName("weather_code")
    val weatherCode: List<Int> = emptyList()
)
