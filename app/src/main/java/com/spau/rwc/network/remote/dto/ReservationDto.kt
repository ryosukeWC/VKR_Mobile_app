package com.spau.rwc.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservationResponse(
    @SerialName("reservation_id") val reservationId: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("restaurant_id") val restaurantId: Int,
    @SerialName("reservation_date") val reservationDate: String,
    @SerialName("reservation_time") val reservationTime: String,
    val guests: Int,
    val status: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class ReservationsListResponse(
    val reservations: List<ReservationResponse>
)