package com.spau.rwc.network.remote.repository

import android.util.Log
import com.spau.rwc.model.IsAdminResponse
import com.spau.rwc.model.Reservation
import com.spau.rwc.model.Restaurant
import com.spau.rwc.network.remote.RestaurantRemoteDataSource
import com.spau.rwc.network.remote.dto.IsAdminDTO
import com.spau.rwc.network.remote.dto.ReservationResponse
import com.spau.rwc.network.remote.dto.RestaurantDto
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RestaurantRepository @Inject constructor(
    private val remoteDataSource: RestaurantRemoteDataSource
) {
    suspend fun getRestaurants(): List<Restaurant> {
        return remoteDataSource.getRestaurants().map { it.toDomain() }
    }

    suspend fun getRestaurantById(id : Int): Restaurant {
        return remoteDataSource.getRestaurant(id).toDomain()
    }

    suspend fun getReservations(email : String): List<Reservation> {
        return remoteDataSource.getReservations(email).map { it.toDomain() }
    }
    suspend fun isAdmin(email: String) : IsAdminResponse {
        return remoteDataSource.isAdmin(email).toDomain()
    }
}

private fun IsAdminDTO.toDomain() : IsAdminResponse {
    return IsAdminResponse(
        isAdmin = isAdmin
    )
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

private fun ReservationResponse.toDomain(): Reservation {
    return Reservation(
        id = reservationId,
        userId = userId,
        restaurantId = restaurantId,
        date = LocalDate.parse(reservationDate),
        time = toTime(reservationTime),
        guests = guests,
        status = status,
        createdAt = createdAt
    )
}

private fun toTime(str: String) : LocalTime {

    val zonedDateTime = ZonedDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME)

    val localZoned = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

    val localTime: LocalTime = localZoned.toLocalTime()

    return localTime
}