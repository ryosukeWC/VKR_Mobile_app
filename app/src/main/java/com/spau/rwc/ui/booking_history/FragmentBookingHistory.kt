package com.spau.rwc.ui.booking_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.spau.rwc.databinding.FragmentBookingHistoryBinding
import com.spau.rwc.model.BookingItem

class FragmentBookingHistory : Fragment() {
    private var _binding: FragmentBookingHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BookingHistoryAdapter

    private val bookingsViewModel: BookingsViewModel by activityViewModels()

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

        setupRecyclerView()
        setupClickListeners()

        bookingsViewModel.bookings.observe(viewLifecycleOwner) { bookings ->
            adapter = BookingHistoryAdapter(bookings.toMutableList()) { bookingItem ->
                showBookingDetails(bookingItem)
            }
            binding.restaurantsRecyclerView.adapter = adapter
        }
    }

    private val bookingItems = mutableListOf(
        BookingItem(
            "Ambrosia Hotel & Restaurant",
            "kazi Deiry, Taiger Pass\nChittagong",
            "12/05/2023",
            "19:00", // Добавляем время
            "Confirmed"
        ),
        BookingItem(
            "Tava Restaurant",
            "Zakir Hossain Rd,\nChittagong",
            "10/05/2023",
            "20:30", // Добавляем время
            "Completed"
        ),
        BookingItem(
            "Haatkhola",
            "6 Surson Road,\nChittagong",
            "08/05/2023",
            "18:00", // Добавляем время
            "Cancelled"
        )
    )

    private fun setupRecyclerView() {
        adapter = BookingHistoryAdapter(bookingItems) { bookingItem ->
            showBookingDetails(bookingItem)
        }

        binding.restaurantsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FragmentBookingHistory.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.bookingMoreButton.setOnClickListener {
            // Обработка нажатия кнопки "Booking more"
            bookMoreRestaurants()
        }
    }

    private fun showBookingDetails(bookingItem: BookingItem) {
        // Реализация перехода к деталям бронирования
        // Например:
        // val action = FragmentBookingHistoryDirections.actionToBookingDetails(bookingItem)
        // findNavController().navigate(action)
    }

    private fun bookMoreRestaurants() {
        // Реализация перехода к экрану бронирования
        // Например:
        // findNavController().navigate(R.id.action_to_booking_fragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}