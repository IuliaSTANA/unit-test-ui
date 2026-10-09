package org.me.awa.ui.navigation

import android.net.Uri

object DeepLinkParser {
    fun parse(uri: Uri?): AppNavKey? {
        if (uri == null) return null

        val isHttps = uri.scheme == "https" && uri.host == "org.me.awa" && uri.path == "/weather"
        val isCustomScheme = uri.scheme == "awa" && uri.host == "weather"

        if (isHttps || isCustomScheme) {
            val name = uri.getQueryParameter("name") ?: uri.getQueryParameter("location") ?: "Unknown"
            val lat = uri.getQueryParameter("lat")?.toDoubleOrNull()
                ?: uri.getQueryParameter("latitude")?.toDoubleOrNull() ?: 0.0
            val lon = uri.getQueryParameter("lon")?.toDoubleOrNull()
                ?: uri.getQueryParameter("longitude")?.toDoubleOrNull() ?: 0.0

            return AppNavKey.WeatherDetails(name = name, latitude = lat, longitude = lon)
        }

        return null
    }
}
