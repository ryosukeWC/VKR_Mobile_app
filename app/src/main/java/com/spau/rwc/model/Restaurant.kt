package com.spau.rwc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Restaurant(
    val id: Long,
    val name: String,
    val address: String,
    val phone: String,
    val description: String,
    val logoUrl: String,
    val rating: Float,
    val openTime: String,
    val closeTime: String
) : Parcelable