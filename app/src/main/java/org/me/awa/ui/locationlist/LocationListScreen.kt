package org.me.awa.ui.locationlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.me.awa.R
import org.me.awa.domain.model.FavoriteLocation
import org.me.awa.ui.theme.AwaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    locations: List<FavoriteLocation>,
    onLocationSelected: (FavoriteLocation) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.droidcon_locations_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back_button)
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(locations, key = { it.id }) { location ->
                LocationCard(
                    location = location,
                    onClick = { onLocationSelected(location) }
                )
            }
        }
    }
}

@Composable
private fun LocationCard(
    location: FavoriteLocation,
    onClick: () -> Unit,
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
            Column {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lat: ${"%.2f".format(location.latitude)}, Lon: ${"%.2f".format(location.longitude)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "→",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

class LocationListPreviewParameterProvider : PreviewParameterProvider<List<FavoriteLocation>> {
    override val values: Sequence<List<FavoriteLocation>> = sequenceOf(
        emptyList(),
        listOf(
            FavoriteLocation("droidcon_berlin", "Droidcon Berlin", 52.5200, 13.4050),
            FavoriteLocation("droidcon_london", "Droidcon London", 51.5074, -0.1278),
            FavoriteLocation("droidcon_sf", "Droidcon SF", 37.7749, -122.4194),
            FavoriteLocation("droidcon_nyc", "Droidcon NYC", 40.7128, -74.0060)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun LocationListScreenPreview(
    @PreviewParameter(LocationListPreviewParameterProvider::class) locations: List<FavoriteLocation>
) {
    AwaTheme {
        LocationListScreen(
            locations = locations,
            onLocationSelected = {},
            onBackClick = {}
        )
    }
}

