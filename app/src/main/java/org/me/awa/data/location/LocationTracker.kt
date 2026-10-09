package org.me.awa.data.location

import org.me.awa.domain.model.LocationData

interface LocationTracker {
    suspend fun getCurrentLocation(): LocationData?
    fun hasLocationPermission(): Boolean
}
