package org.me.awa.domain.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val cityName: String? = null
)
