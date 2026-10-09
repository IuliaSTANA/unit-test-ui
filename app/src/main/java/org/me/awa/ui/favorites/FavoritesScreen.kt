package org.me.awa.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.me.awa.R
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.ui.favorites.FavoritesScreenTags.ROOT
import org.me.awa.ui.theme.AwaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favorites: List<FavoriteItemUiState>,
    isLoadingLocation: Boolean,
    onAddLocationClick: () -> Unit,
    onAddCurrentLocationClick: () -> Unit,
    onLocationClick: (FavoriteLocation) -> Unit,
    onRemoveFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                modifier = Modifier.testTag(FavoritesScreenTags.TITLE)
            )
        },
        modifier = modifier
            .fillMaxSize()
            .testTag(ROOT)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .semantics { isTraversalGroup = true },
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAddLocationClick,
                    modifier = Modifier
                        .testTag(FavoritesScreenTags.ADD_NEW_BUTTON)
                        .semantics { traversalIndex = 1f
                            testTag = FavoritesScreenTags.ADD_NEW_BUTTON
                        }
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(stringResource(R.string.btn_add_favorite_location))
                }

                OutlinedButton(
                    enabled = !isLoadingLocation,
                    onClick = onAddCurrentLocationClick,
                    modifier = Modifier
                        .fillMaxHeight()
                        .testTag(FavoritesScreenTags.ADD_CURRENT_LOCATION_BUTTON)
                        .semantics { traversalIndex = 2f }
                        .weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(stringResource(R.string.btn_add_current_location))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoadingLocation) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(FavoritesScreenTags.LOADING_PROGRESS)
                )
            }
            if (favorites.isEmpty()) {
                EmptyFavoritesView()
            } else {
                Text(
                    text = stringResource(R.string.favorites_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(FavoritesScreenTags.FAVORITES_LIST)
                ) {
                    items(favorites, key = { it.location.id }) { item ->
                        FavoriteItemCard(
                            item = item,
                            onClick = { onLocationClick(item.location) },
                            onRemove = { onRemoveFavorite(item.location.id) },
                            modifier = Modifier.testTag(FavoritesScreenTags.favoriteItemTag(item.location.id))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .testTag(FavoritesScreenTags.EMPTY_STATE)
            .semantics(mergeDescendants = true) {},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🌤️",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.favorites_empty_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.favorites_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FavoriteItemCard(
    item: FavoriteItemUiState,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.location.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (item.isLoading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.weather_fetching_data),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else if (item.error != null) {
                    Text(
                        text = item.error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        item.condition?.let { cond ->
                            Text(
                                text = cond.emoji,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = cond.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            if (item.temperature != null && !item.isLoading) {
                Text(
                    text = "${"%.1f".format(item.temperature)}°C",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.testTag(FavoritesScreenTags.favoriteItemDeleteTag(item.location.id))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.cd_delete_favorite),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

class FavoritesScreenPreviewParameterProvider :
    PreviewParameterProvider<List<FavoriteItemUiState>> {
    override val values: Sequence<List<FavoriteItemUiState>> = sequenceOf(
        emptyList(),
        listOf(
            FavoriteItemUiState(
                location = FavoriteLocation("1", "Berlin", 52.52, 13.405),
                temperature = 21.5,
                condition = WeatherCondition.CLEAR_SKY,
                isLoading = false
            ),
            FavoriteItemUiState(
                location = FavoriteLocation("2", "London", 51.507, -0.127),
                temperature = null,
                condition = null,
                isLoading = true
            ),
            FavoriteItemUiState(
                location = FavoriteLocation("3", "Tokyo", 35.676, 139.65),
                temperature = null,
                condition = null,
                isLoading = false,
                error = "Failed to fetch weather"
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview(
    @PreviewParameter(FavoritesScreenPreviewParameterProvider::class) favorites: List<FavoriteItemUiState>
) {
    AwaTheme {
        FavoritesScreen(
            favorites = favorites,
            isLoadingLocation = false,
            onAddLocationClick = {},
            onAddCurrentLocationClick = {},
            onLocationClick = {},
            onRemoveFavorite = {},
        )
    }
}

object FavoritesScreenTags {
    const val ROOT = "favorites:root"
    const val TITLE = "favorites:title"
    const val ADD_NEW_BUTTON = "favorites:add_new"
    const val ADD_CURRENT_LOCATION_BUTTON = "favorites:add_current_location"
    const val FAVORITES_LIST = "favorites:list"
    const val LOADING_PROGRESS = "favorites:loading_progress"
    const val EMPTY_STATE = "favorites:empty_state"

    fun favoriteItemTag(id: String) = "favorites:item:$id"
    fun favoriteItemDeleteTag(id: String) = "favorites:item_delete:$id"
}