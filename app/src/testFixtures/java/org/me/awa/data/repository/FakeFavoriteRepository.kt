package org.me.awa.data.repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import org.me.awa.domain.model.FavoriteLocation
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class FakeFavoritesRepository @Inject constructor() : FavoritesRepository {
    private val _favorites = MutableStateFlow<List<FavoriteLocation>>(emptyList())
    override val favorites: Flow<List<FavoriteLocation>> = _favorites

    override suspend fun addFavorite(location: FavoriteLocation) {
        val current = _favorites.value.toMutableList()
        if (current.none { it.id == location.id || (abs(it.latitude - location.latitude) < 0.001 && abs(it.longitude - location.longitude) < 0.001) }) {
            current.add(location)
            _favorites.value = current
        }
    }

    override suspend fun removeFavorite(id: String) {
        _favorites.value = _favorites.value.filterNot { it.id == id }
    }

    override suspend fun removeFavoriteByCoordinates(latitude: Double, longitude: Double) {
        _favorites.value = _favorites.value.filterNot {
            abs(it.latitude - latitude) < 0.001 && abs(it.longitude - longitude) < 0.001
        }
    }

    override suspend fun isFavorite(latitude: Double, longitude: Double): Boolean {
        return _favorites.value.any {
            abs(it.latitude - latitude) < 0.001 && abs(it.longitude - longitude) < 0.001
        }
    }
}