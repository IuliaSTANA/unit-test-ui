package org.me.awa.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteLocation(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean = false
)
