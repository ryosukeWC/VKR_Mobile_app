package com.spau.rwc.model

data class ReservationItem(
    val reservationNumber: String,
    val dateTime: String,
    val tableNumber: String,
    val peopleCount: String,
    val userInitial: String = "А",
    var status : String
)
