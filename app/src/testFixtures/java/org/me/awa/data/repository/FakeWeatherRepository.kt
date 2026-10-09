package org.me.awa.data.repository

import org.me.awa.domain.model.WeatherData
import org.me.awa.fakeWeather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeWeatherRepository @Inject constructor() : WeatherRepository {
    var weatherResult: Result<WeatherData> = Result.success(fakeWeather)
    var fetchCount: Int = 0

    override suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherData> {
        fetchCount++
        return weatherResult
    }
}
