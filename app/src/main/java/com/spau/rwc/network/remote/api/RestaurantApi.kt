package com.spau.rwc.network.remote.api

import com.spau.rwc.network.remote.dto.RestaurantDto
import retrofit2.http.GET

interface RestaurantApi {
    @GET("restaurants/")
    suspend fun getRestaurants(): List<RestaurantDto>
}