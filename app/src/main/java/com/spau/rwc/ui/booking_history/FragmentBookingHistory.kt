package com.spau.rwc.ui.booking_history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.spau.rwc.databinding.FragmentBookingHistoryBinding
import com.spau.rwc.model.BookingItem
import com.spau.rwc.model.Reservation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class FragmentBookingHistory : Fragment() {


    private var _binding: FragmentBookingHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BookingHistoryAdapter

    private val bookingsViewModel: BookingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingsViewModel.loadRestaurants()

        setupRecyclerView()
        setupClickListeners()
        observeReservations()
        observeLoadingState()
    }

    private fun setupRecyclerView() {
        adapter = BookingHistoryAdapter(mutableListOf()) { reservation ->
            showReservationDetails(reservation) // Изменили параметр
        }

        binding.restaurantsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FragmentBookingHistory.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.bookingMoreButton.setOnClickListener {
            bookMoreRestaurants()
        }
    }

    private fun bookMoreRestaurants() {
        // Реализация перехода к экрану бронирования
        // Например:
        // findNavController().navigate(R.id.action_to_booking_fragment)
    }

    private fun showReservationDetails(reservation: Reservation) {
        // Реализация перехода к деталям бронирования
        // Например:
        // val action = FragmentBookingHistoryDirections.actionToBookingDetails(reservation)
        // findNavController().navigate(action)
    }

    private fun observeReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookingsViewModel.reservations.collect { reservations ->
                    if (reservations.isEmpty()) {
                        val test = listOf(
                            Reservation(1, 1, 1, LocalDate.parse("2023-12-31"), LocalTime.parse("02:00"), 2, "pending", "now")
                        )
                        adapter.update(test)
                    } else {
                        adapter.update(reservations)
                    }
                }
            }
        }
    }

    private fun observeLoadingState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookingsViewModel.isLoading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}