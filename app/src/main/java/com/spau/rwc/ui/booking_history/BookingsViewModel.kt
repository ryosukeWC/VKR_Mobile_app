package com.spau.rwc.ui.booking_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spau.rwc.authentication.SessionManager
import com.spau.rwc.model.BookingItem
import com.spau.rwc.model.Reservation
import com.spau.rwc.model.Restaurant
import com.spau.rwc.network.remote.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _restaurantsMap = MutableStateFlow<Map<Int, Restaurant>>(emptyMap())
    val restaurantsMap: StateFlow<Map<Int, Restaurant>> = _restaurantsMap

    fun loadRestaurantsAndReservations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val email = getUserEmail()
                val reservations = repository.getReservations(email)
                _reservations.value = reservations

                val allRestaurantIds = reservations.map { it.restaurantId }.distinct()
                val restaurants = allRestaurantIds.map { id ->
                    repository.getRestaurantById(id)
                }
                _restaurantsMap.value = restaurants.associateBy { it.id.toInt() }
            } catch (e: Exception) {
                _error.emit("Failed to load: ${e.message}")
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