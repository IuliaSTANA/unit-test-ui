package org.me.awa.ui.level03

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.me.awa.data.location.FakeLocationTracker
import org.me.awa.data.location.LocationTracker
import org.me.awa.data.repository.FakeFavoritesRepository
import org.me.awa.data.repository.FakeWeatherRepository
import org.me.awa.data.repository.FavoritesRepository
import org.me.awa.data.repository.WeatherRepository
import org.me.awa.di.LocationModule
import org.me.awa.di.RepositoryModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class, LocationModule::class]
)
abstract class TestRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(fake: FakeWeatherRepository): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(fake: FakeFavoritesRepository): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindLocationTracker(fake: FakeLocationTracker): LocationTracker
}
