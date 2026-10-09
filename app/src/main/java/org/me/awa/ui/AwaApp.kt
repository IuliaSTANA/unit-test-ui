package org.me.awa.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.me.awa.ui.favorites.FavoritesRoute
import org.me.awa.ui.locationlist.LocationListRoute
import org.me.awa.ui.navigation.AppNavKey
import org.me.awa.ui.permission.LocationPermissionRoute
import org.me.awa.ui.weather.WeatherDetailsRoute

@Composable
fun AwaApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navigator = viewModel.navigator

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.navigateBack() },
        modifier = modifier,
        entryProvider = entryProvider {
            entry<AppNavKey.Favorites> {
                FavoritesRoute()
            }

            entry<AppNavKey.LocationList> {
                LocationListRoute()
            }

            entry<AppNavKey.WeatherDetails> { key ->
                WeatherDetailsRoute(key = key)
            }

            entry<AppNavKey.LocationPermission> {
                LocationPermissionRoute(onBack = { navigator.navigateBack() })
            }
        }
    )
}
