package com.spau.rwc.di

import com.spau.rwc.network.remote.RestaurantRemoteDataSource
import com.spau.rwc.network.remote.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // или другой подходящий компонент
object RepositoryModule {

    @Provides
    fun provideRestaurantRepository(
        remoteDataSource: RestaurantRemoteDataSource
    ): RestaurantRepository {
        return RestaurantRepository(remoteDataSource)
    }
}