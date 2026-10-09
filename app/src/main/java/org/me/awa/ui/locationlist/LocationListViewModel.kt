package org.me.awa.ui.locationlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.me.awa.domain.model.DroidconLocations
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.ui.navigation.AppNavKey
import org.me.awa.ui.navigation.AppNavigator
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(
    private val navigator: AppNavigator
) : ViewModel() {

    val locations: List<FavoriteLocation> = DroidconLocations.list

    fun onLocationSelected(location: FavoriteLocation) {
        navigator.navigateTo(
            AppNavKey.WeatherDetails(
                name = location.name,
                latitude = location.latitude,
                longitude = location.longitude
            )
        )
    }

    fun onBackClick() {
        navigator.navigateBack()
    }
}
