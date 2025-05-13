package com.spau.rwc.network.remote

import com.spau.rwc.network.remote.api.RestaurantApi
import com.spau.rwc.network.remote.dto.RestaurantDto

class RestaurantRemoteDataSource(
    private val api: RestaurantApi
) {
    suspend fun getRestaurants(): List<RestaurantDto> {
        return api.getRestaurants()
    }
}