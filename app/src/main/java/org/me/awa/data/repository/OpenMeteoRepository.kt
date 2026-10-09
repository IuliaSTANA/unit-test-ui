package org.me.awa.data.repository

import org.me.awa.data.api.OpenMeteoApi
import org.me.awa.domain.model.HourlyForecastData
import org.me.awa.domain.model.LocationData
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.domain.model.WeatherData
import javax.inject.Inject

class OpenMeteoRepository @Inject constructor(
    private val api: OpenMeteoApi
) : WeatherRepository {

    override suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherData> {
        return runCatching {
            val response = api.getWeatherForecast(latitude = latitude, longitude = longitude)
            val current = response.current ?: throw IllegalStateException("Current weather data unavailable")

            val hourlyList = mutableListOf<HourlyForecastData>()
            response.hourly?.let { hourly ->
                val count = minOf(hourly.time.size, hourly.temperature.size, 24)
                for (i in 0 until count) {
                    val code = hourly.weatherCode.getOrElse(i) { 0 }
                    hourlyList.add(
                        HourlyForecastData(
                            time = hourly.time[i],
                            temperature = hourly.temperature[i],
                            condition = WeatherCondition.fromWmoCode(code)
                        )
                    )
                }
            }

            WeatherData(
                location = LocationData(latitude = latitude, longitude = longitude),
                currentTemperature = current.temperature,
                condition = WeatherCondition.fromWmoCode(current.weatherCode),
                relativeHumidity = current.relativeHumidity,
                windSpeed = current.windSpeed,
                hourlyForecast = hourlyList
            )
        }
    }
}
