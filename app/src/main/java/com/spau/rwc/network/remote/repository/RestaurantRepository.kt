package com.spau.rwc.network.remote.repository

import com.spau.rwc.model.Restaurant
import com.spau.rwc.network.remote.RestaurantRemoteDataSource
import com.spau.rwc.network.remote.dto.RestaurantDto
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val remoteDataSource: RestaurantRemoteDataSource
) {
    suspend fun getRestaurants(): List<Restaurant> {
        return remoteDataSource.getRestaurants().map { it.toDomain() }
    }
}

private fun RestaurantDto.toDomain(): Restaurant {
    return Restaurant(
        id = id,
        name = name,
        address = address,
        phone = phone,
        description = description,
        logoUrl = logoUrl,
        rating = rating,
        openTime = openTime,
        closeTime = closeTime
    )
}