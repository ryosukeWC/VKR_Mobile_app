package com.spau.rwc.network.remote.api

import com.spau.rwc.network.remote.dto.ReservationResponse
import com.spau.rwc.network.remote.dto.RestaurantDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApi {
    @GET("restaurants/")
    suspend fun getRestaurants(): List<RestaurantDto>

    @GET("reservations/")
    suspend fun getReservationsByEmail(
        @Query("email") email: String
    ): List<ReservationResponse>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(
        @Path("id") restaurantId: Int
    ): RestaurantDto
}