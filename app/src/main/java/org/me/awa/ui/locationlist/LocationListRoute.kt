package org.me.awa.ui.locationlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun LocationListRoute(
    modifier: Modifier = Modifier,
    viewModel: LocationListViewModel = hiltViewModel()
) {
    LocationListScreen(
        locations = viewModel.locations,
        onLocationSelected = viewModel::onLocationSelected,
        onBackClick = viewModel::onBackClick,
        modifier = modifier
    )
}
