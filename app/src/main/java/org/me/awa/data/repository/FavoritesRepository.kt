package org.me.awa.data.repository

import kotlinx.coroutines.flow.Flow
import org.me.awa.domain.model.FavoriteLocation

interface FavoritesRepository {

    val favorites: Flow<List<FavoriteLocation>>

    suspend fun addFavorite(location: FavoriteLocation)

    suspend fun removeFavorite(id: String)

    suspend fun removeFavoriteByCoordinates(latitude: Double, longitude: Double)

    suspend fun isFavorite(latitude: Double, longitude: Double): Boolean
}
