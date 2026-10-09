package org.me.awa.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FavoritesRoute(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favoritesState.collectAsStateWithLifecycle()
    val isLoadingLocation by viewModel.isLoadingLocation.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refreshAllFavorites()
        onPauseOrDispose { }
    }

    FavoritesScreen(
        favorites = favorites,
        isLoadingLocation = isLoadingLocation,
        onAddLocationClick = viewModel::onAddLocationClick,
        onAddCurrentLocationClick = viewModel::addCurrentLocationToFavorites,
        onLocationClick = viewModel::onLocationClick,
        onRemoveFavorite = viewModel::removeFavorite,
        modifier = modifier
    )
}
