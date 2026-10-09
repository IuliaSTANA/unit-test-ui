package org.me.awa.data.location

import org.me.awa.domain.model.LocationData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeLocationTracker @Inject constructor() : LocationTracker {
    var hasPermission: Boolean = true
    var currentLocation: LocationData? = LocationData(latitude = 52.5200, longitude = 13.4050, cityName = "Berlin")

    override suspend fun getCurrentLocation(): LocationData? = currentLocation

    override fun hasLocationPermission(): Boolean = hasPermission
}
