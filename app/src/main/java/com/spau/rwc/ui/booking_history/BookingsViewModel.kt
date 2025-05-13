package com.spau.rwc.ui.booking_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spau.rwc.model.BookingItem

class BookingsViewModel : ViewModel() {
    private val _bookings = MutableLiveData<List<BookingItem>>()
    val bookings: LiveData<List<BookingItem>> = _bookings

    fun addBooking(newBooking: BookingItem) {
        val current = _bookings.value.orEmpty().toMutableList()
        current.add(0, newBooking)
        _bookings.value = current
    }
}