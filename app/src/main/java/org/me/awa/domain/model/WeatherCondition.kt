package org.me.awa.domain.model

enum class WeatherCondition(val description: String, val emoji: String) {
    CLEAR_SKY("Clear sky", "☀️"),
    MAINLY_CLEAR("Mainly clear", "🌤️"),
    PARTLY_CLOUDY("Partly cloudy", "⛅"),
    OVERCAST("Overcast", "☁️"),
    FOG("Foggy", "🌫️"),
    DRIZZLE("Drizzle", "🌧️"),
    RAIN("Rainy", "🌧️"),
    SNOW("Snowy", "❄️"),
    THUNDERSTORM("Thunderstorm", "⛈️"),
    UNKNOWN("Unknown", "🌡️");

    companion object {
        fun fromWmoCode(code: Int): WeatherCondition = when (code) {
            0 -> CLEAR_SKY
            1 -> MAINLY_CLEAR
            2 -> PARTLY_CLOUDY
            3 -> OVERCAST
            45, 48 -> FOG
            51, 53, 55, 56, 57 -> DRIZZLE
            61, 63, 65, 66, 67, 80, 81, 82 -> RAIN
            71, 73, 75, 77, 85, 86 -> SNOW
            95, 96, 99 -> THUNDERSTORM
            else -> UNKNOWN
        }
    }
}
