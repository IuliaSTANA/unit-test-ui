package org.me.awa.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.me.awa.R
import org.me.awa.domain.model.HourlyForecastData
import org.me.awa.domain.model.LocationData
import org.me.awa.domain.model.WeatherCondition
import org.me.awa.domain.model.WeatherData
import org.me.awa.ui.theme.AwaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    locationName: String,
    uiState: WeatherUiState,
    isFavorite: Boolean,
    onRefresh: () -> Unit,
    onToggleFavorite: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(locationName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleFavorite, Modifier.testTag(WeatherScreenTags.TOGGLE_FAVORITE)) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) stringResource(R.string.cd_favorite_icon) else stringResource(R.string.cd_not_favorite_icon),
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is WeatherUiState.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(stringResource(R.string.weather_fetching_data), style = MaterialTheme.typography.bodyMedium)
                    }
                }

                is WeatherUiState.PermissionRequired -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.weather_location_permission_needed),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text(stringResource(R.string.btn_grant_permission))
                        }
                    }
                }

                is WeatherUiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text(stringResource(R.string.btn_retry))
                        }
                    }
                }

                is WeatherUiState.Success -> {
                    WeatherContent(
                        weather = uiState.weather,
                        isFavorite = isFavorite,
                        onRefresh = onRefresh,
                        onToggleFavorite = onToggleFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(
    weather: WeatherData,
    isFavorite: Boolean,
    onRefresh: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.location.cityName ?: stringResource(R.string.weather_current_location),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${weather.condition.emoji} ${weather.condition.description}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${"%.1f".format(weather.currentTemperature)}°C",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                weather.relativeHumidity?.let { humidity ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.weather_humidity), style = MaterialTheme.typography.labelMedium)
                        Text("$humidity%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                }

                weather.windSpeed?.let { wind ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.weather_wind_speed), style = MaterialTheme.typography.labelMedium)
                        Text("${"%.1f".format(wind)} km/h", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (weather.hourlyForecast.isNotEmpty()) {
            Text(
                text = stringResource(R.string.weather_hourly_forecast),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(weather.hourlyForecast) { item ->
                    HourlyForecastItem(item = item)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onRefresh,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(stringResource(R.string.btn_refresh_weather))
            }

            Button(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = if (isFavorite) stringResource(R.string.btn_remove_from_favorites) else stringResource(R.string.btn_add_to_favorites)
                )
            }
        }
    }
}

@Composable
private fun HourlyForecastItem(item: HourlyForecastData) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val timeFormatted = item.time.substringAfter("T").take(5)
            Text(text = timeFormatted, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.condition.emoji, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${"%.0f".format(item.temperature)}°", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}

data class WeatherScreenPreviewData(
    val locationName: String,
    val uiState: WeatherUiState,
    val isFavorite: Boolean
)

class WeatherScreenPreviewParameterProvider : PreviewParameterProvider<WeatherScreenPreviewData> {
    override val values: Sequence<WeatherScreenPreviewData> = sequenceOf(
        WeatherScreenPreviewData(
            locationName = "Berlin",
            uiState = WeatherUiState.Success(
                weather = WeatherData(
                    location = LocationData(latitude = 52.52, longitude = 13.405, cityName = "Berlin"),
                    currentTemperature = 22.5,
                    condition = WeatherCondition.CLEAR_SKY,
                    relativeHumidity = 45,
                    windSpeed = 12.3,
                    hourlyForecast = listOf(
                        HourlyForecastData(time = "2025-05-10T12:00", temperature = 21.0, condition = WeatherCondition.CLEAR_SKY),
                        HourlyForecastData(time = "2025-05-10T13:00", temperature = 22.5, condition = WeatherCondition.PARTLY_CLOUDY),
                        HourlyForecastData(time = "2025-05-10T14:00", temperature = 23.0, condition = WeatherCondition.PARTLY_CLOUDY),
                        HourlyForecastData(time = "2025-05-10T15:00", temperature = 22.0, condition = WeatherCondition.OVERCAST),
                        HourlyForecastData(time = "2025-05-10T16:00", temperature = 20.5, condition = WeatherCondition.RAIN)
                    )
                )
            ),
            isFavorite = true
        ),
        WeatherScreenPreviewData(
            locationName = "London",
            uiState = WeatherUiState.Success(
                weather = WeatherData(
                    location = LocationData(latitude = 51.507, longitude = -0.127, cityName = "London"),
                    currentTemperature = 15.0,
                    condition = WeatherCondition.RAIN,
                    relativeHumidity = 80,
                    windSpeed = 22.0,
                    hourlyForecast = listOf(
                        HourlyForecastData(time = "2025-05-10T12:00", temperature = 14.0, condition = WeatherCondition.RAIN),
                        HourlyForecastData(time = "2025-05-10T13:00", temperature = 15.0, condition = WeatherCondition.DRIZZLE),
                        HourlyForecastData(time = "2025-05-10T14:00", temperature = 15.5, condition = WeatherCondition.OVERCAST)
                    )
                )
            ),
            isFavorite = false
        ),
        WeatherScreenPreviewData(
            locationName = "Tokyo",
            uiState = WeatherUiState.Loading,
            isFavorite = false
        ),
        WeatherScreenPreviewData(
            locationName = "San Francisco",
            uiState = WeatherUiState.PermissionRequired,
            isFavorite = false
        ),
        WeatherScreenPreviewData(
            locationName = "New York",
            uiState = WeatherUiState.Error("Network error: Unable to connect to server"),
            isFavorite = false
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenPreview(
    @PreviewParameter(WeatherScreenPreviewParameterProvider::class) previewData: WeatherScreenPreviewData
) {
    AwaTheme {
        WeatherScreen(
            locationName = previewData.locationName,
            uiState = previewData.uiState,
            isFavorite = previewData.isFavorite,
            onRefresh = {},
            onToggleFavorite = {},
            onBackClick = {}
        )
    }
}

object WeatherScreenTags{
    const val ROOT = "weather:root"
    const val TOGGLE_FAVORITE = "weather:toggleFavorite"
}