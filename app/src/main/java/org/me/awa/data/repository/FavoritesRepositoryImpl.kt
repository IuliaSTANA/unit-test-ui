package org.me.awa.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.me.awa.domain.model.FavoriteLocation
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : FavoritesRepository {

    private object PreferencesKeys {
        val FAVORITES = stringPreferencesKey("favorite_locations")
    }

    override val favorites: Flow<List<FavoriteLocation>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val jsonString = preferences[PreferencesKeys.FAVORITES] ?: return@map emptyList()
            try {
                json.decodeFromString<List<FavoriteLocation>>(jsonString)
            } catch (_: Exception) {
                emptyList()
            }
        }

    override suspend fun addFavorite(location: FavoriteLocation) {
        dataStore.edit { preferences ->
            val currentList = getFavoritesFromPreferences(preferences).toMutableList()
            if (currentList.none { (it.id == location.id) || isSameLocation(it, location.latitude, location.longitude) }) {
                currentList.add(location)
                preferences[PreferencesKeys.FAVORITES] = json.encodeToString(currentList)
            }
        }
    }

    override suspend fun removeFavorite(id: String) {
        dataStore.edit { preferences ->
            val currentList = getFavoritesFromPreferences(preferences)
            val updatedList = currentList.filterNot { it.id == id }
            preferences[PreferencesKeys.FAVORITES] = json.encodeToString(updatedList)
        }
    }

    override suspend fun removeFavoriteByCoordinates(latitude: Double, longitude: Double) {
        dataStore.edit { preferences ->
            val currentList = getFavoritesFromPreferences(preferences)
            val updatedList = currentList.filterNot { isSameLocation(it, latitude, longitude) }
            preferences[PreferencesKeys.FAVORITES] = json.encodeToString(updatedList)
        }
    }

    override suspend fun isFavorite(latitude: Double, longitude: Double): Boolean {
        return favorites.first().any { isSameLocation(it, latitude, longitude) }
    }

    private fun getFavoritesFromPreferences(preferences: Preferences): List<FavoriteLocation> {
        val jsonString = preferences[PreferencesKeys.FAVORITES] ?: return emptyList()
        return try {
            json.decodeFromString(jsonString)
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun isSameLocation(loc: FavoriteLocation, latitude: Double, longitude: Double): Boolean {
        return abs(loc.latitude - latitude) < 0.001 && abs(loc.longitude - longitude) < 0.001
    }
}
