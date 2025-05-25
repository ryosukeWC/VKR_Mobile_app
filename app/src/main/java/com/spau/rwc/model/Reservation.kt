package com.spau.rwc.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

data class Reservation(
    val id: Int,
    val userId: Int,
    val restaurantId: Int,
    val date: LocalDate,
    val time: LocalTime,
    val guests: Int,
    val status: String,
    val createdAt: String
)