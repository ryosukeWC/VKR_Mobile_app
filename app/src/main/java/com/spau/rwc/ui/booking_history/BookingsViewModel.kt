package com.spau.rwc.ui.booking_history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spau.rwc.authentication.SessionManager
import com.spau.rwc.model.BookingItem
import com.spau.rwc.model.Reservation
import com.spau.rwc.network.remote.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {

    private val sessionManager = SessionManager

    private fun getUserEmail() : String {
        return sessionManager.getCurrentUser()?.email ?: "Unknown email"
    }

    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations: StateFlow<List<Reservation>> = _reservations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    private fun toLocalTime(str : String) {

    }

    fun loadRestaurants() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val email = getUserEmail()
                _reservations.value = repository.getReservations(email)
//                _reservations.value = listOf(
//                    Reservation(
//                        id = 1,
//                        userId = 1,
//                        restaurantId = 1,
//                        date = LocalDate.parse("2023-12-31"),
//                        time = LocalTime.parse("2025-05-17T22:25:22.806042+06:00"),
//                        guests = 4,
//                        status = "pending",
//                        createdAt = "2025-05-17T22:25:22.806042+03:00"
//                    )
//                )
                Log.i("viewmodel test", "Loaded from repo: ${reservations.value}")
            } catch (e: Exception) {
                _error.emit("Failed to load reservations: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _bookings = MutableLiveData<List<BookingItem>>()
    val bookings: LiveData<List<BookingItem>> = _bookings

    fun addBooking(newBooking: BookingItem) {
        val current = _bookings.value.orEmpty().toMutableList()
        current.add(0, newBooking)
        _bookings.value = current
    }
}