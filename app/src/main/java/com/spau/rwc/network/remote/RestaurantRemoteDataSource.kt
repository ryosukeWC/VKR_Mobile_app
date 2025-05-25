package com.spau.rwc.network.remote

import com.spau.rwc.network.remote.api.RestaurantApi
import com.spau.rwc.network.remote.dto.ReservationResponse
import com.spau.rwc.network.remote.dto.RestaurantDto

class RestaurantRemoteDataSource(
    private val api: RestaurantApi
) {
    suspend fun getRestaurants(): List<RestaurantDto> {
        return api.getRestaurants()
    }

    suspend fun getRestaurant(id : Int): RestaurantDto {
        return api.getRestaurantById(id)
    }

    suspend fun getReservations(email : String) : List<ReservationResponse> {
        return api.getReservationsByEmail(email)
    }
}