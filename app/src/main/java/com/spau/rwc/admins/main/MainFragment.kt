package com.spau.rwc.admins.main

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.spau.rwc.databinding.FragmentMainBinding
import com.spau.rwc.model.NumberReservation
import com.spau.rwc.model.ReservationItem


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReservationAdminAdapter
    private var currentFilter: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservationNumbers = listOf(
            NumberReservation("#001"),
            NumberReservation("#004"),
            NumberReservation("#006"),
            NumberReservation("#008"),
            NumberReservation("#011"),
            NumberReservation("#013"),
            NumberReservation("#015")
        )

        setupFilter()

        binding.numbersRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ReservationNumberAdapter(reservationNumbers)
        }

        adapter = ReservationAdminAdapter(emptyList())
        binding.reservationsRv.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = this@MainFragment.adapter
        }
        loadData()
    }

    private fun setupFilter() {
        binding.filterTv.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun showFilterDialog() {
        val filters = arrayOf("Подтверждено", "Отклонено", "Ждет подтверждения")

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите фильтр")
            .setItems(filters) { _, which ->
                currentFilter = filters[which]
                binding.filterTv.text = "Фильтр: $currentFilter"
                loadData()
            }
            .setNeutralButton("Сбросить") { _, _ ->
                currentFilter = null
                binding.filterTv.text = "Фильтр"
                loadData()
            }
            .show()
    }

    private fun loadData() {

        val transition = AutoTransition()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)

        binding.progressBar.visibility = View.VISIBLE
        binding.reservationsRv.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            val filteredList = if (currentFilter != null) {
                getTestData().filter { it.status == currentFilter }
            } else {
                getTestData()
            }

            adapter.updateData(filteredList)
            binding.progressBar.visibility = View.GONE
            binding.reservationsRv.visibility = View.VISIBLE
        }, 1500)
    }

    private fun getTestData(): List<ReservationItem> {
        return listOf(
            ReservationItem(
                reservationNumber = "Бронь #001",
                dateTime = "10 Мая 2025, 11.00",
                tableNumber = "Стол №1",
                peopleCount = "× 2 Человека",
                userInitial = "А",
                status = "Подтверждено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #002",
                dateTime = "10 Мая 2025, 13.45",
                tableNumber = "Стол №4",
                peopleCount = "× 3 Человека",
                userInitial = "Б",
                status = "Отклонено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #003",
                dateTime = "11 Мая 2025, 12.30",
                tableNumber = "Стол №2",
                peopleCount = "× 4 Человека",
                userInitial = "В",
                status = "Ждет подтверждения"
            ),
            ReservationItem(
                reservationNumber = "Бронь #004",
                dateTime = "12 Мая 2025, 14.00",
                tableNumber = "Стол №5",
                peopleCount = "× 2 Человека",
                userInitial = "Г",
                status = "Подтверждено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #005",
                dateTime = "13 Мая 2025, 16.15",
                tableNumber = "Стол №3",
                peopleCount = "× 3 Человека",
                userInitial = "Д",
                status = "Отклонено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #006",
                dateTime = "14 Мая 2025, 10.30",
                tableNumber = "Стол №1",
                peopleCount = "× 1 Человек",
                userInitial = "Е",
                status = "Подтверждено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #007",
                dateTime = "15 Мая 2025, 18.00",
                tableNumber = "Стол №6",
                peopleCount = "× 5 Человек",
                userInitial = "Ж",
                status = "Ждет подтверждения"
            ),
            ReservationItem(
                reservationNumber = "Бронь #008",
                dateTime = "16 Мая 2025, 12.45",
                tableNumber = "Стол №2",
                peopleCount = "× 2 Человека",
                userInitial = "З",
                status = "Подтверждено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #009",
                dateTime = "17 Мая 2025, 19.30",
                tableNumber = "Стол №4",
                peopleCount = "× 4 Человека",
                userInitial = "И",
                status = "Отклонено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #010",
                dateTime = "18 Мая 2025, 15.00",
                tableNumber = "Стол №3",
                peopleCount = "× 2 Человека",
                userInitial = "К",
                status = "Ждет подтверждения"
            ),
            ReservationItem(
                reservationNumber = "Бронь #011",
                dateTime = "19 Мая 2025, 17.45",
                tableNumber = "Стол №5",
                peopleCount = "× 3 Человека",
                userInitial = "Л",
                status = "Подтверждено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #012",
                dateTime = "20 Мая 2025, 14.20",
                tableNumber = "Стол №1",
                peopleCount = "× 2 Человека",
                userInitial = "М",
                status = "Отклонено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #013",
                dateTime = "21 Мая 2025, 13.10",
                tableNumber = "Стол №2",
                peopleCount = "× 1 Человек",
                userInitial = "Н",
                status = "Подтверждено"
            ),
            ReservationItem(
                reservationNumber = "Бронь #014",
                dateTime = "22 Мая 2025, 20.00",
                tableNumber = "Стол №6",
                peopleCount = "× 6 Человек",
                userInitial = "О",
                status = "Ждет подтверждения"
            ),
            ReservationItem(
                reservationNumber = "Бронь #015",
                dateTime = "23 Мая 2025, 11.30",
                tableNumber = "Стол №4",
                peopleCount = "× 2 Человека",
                userInitial = "П",
                status = "Подтверждено"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}