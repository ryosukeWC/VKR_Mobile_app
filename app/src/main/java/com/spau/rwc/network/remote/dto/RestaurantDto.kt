package com.spau.rwc.network.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDto(
    @SerialName("restaurant_id") val id: Long,
    @SerialName("restaurant_name") val name: String,
    @SerialName("restaurant_address") val address: String,
    @SerialName("restaurant_phone") val phone: String,
    @SerialName("restaurant_description") val description: String,
    @SerialName("restaurant_logo") val logoUrl: String,
    @SerialName("restaurant_rating") val rating: Float,
    @SerialName("open_time") val openTime: String,
    @SerialName("close_time") val closeTime: String,
    @SerialName("created_at") val createdAt: String
)
