package org.me.awa.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.me.awa.data.repository.FavoritesRepository
import org.me.awa.data.repository.FavoritesRepositoryImpl
import org.me.awa.data.repository.OpenMeteoRepository
import org.me.awa.data.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        openMeteoRepository: OpenMeteoRepository
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
}
