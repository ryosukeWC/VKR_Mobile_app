package com.spau.rwc.ui.restaurnat_details

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.spau.rwc.R
import com.spau.rwc.common.OpenMaps
import com.spau.rwc.databinding.FragmentRestaurnatDetailsBinding
import com.spau.rwc.model.BookingItem
import com.spau.rwc.model.Restaurant
import com.spau.rwc.ui.booking_history.BookingsViewModel
import com.spau.rwc.ui.booking_history.FragmentBookingHistory
import com.spau.rwc.ui.restaurnat_details.timeslots.TimeSlotAdapter
import com.spau.rwc.ui.restaurnat_details.util.ViewDimensions
import java.util.Calendar
import java.util.Locale

class RestaurnatDetails : Fragment() {

    companion object {
        fun newInstance() = RestaurnatDetails()
    }

    private val viewModel: RestaurnatDetailsViewModel by viewModels()
    private lateinit var binding : FragmentRestaurnatDetailsBinding

    private val bookingsViewModel: BookingsViewModel by activityViewModels()

    private lateinit var restaurant: Restaurant

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private var selectedPeople = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurnatDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurant = arguments?.getParcelable("restaurant") ?: throw IllegalStateException("Restaurant not passed")

        val viewDate = binding.viewDate
        val viewTime = binding.viewTime
        val viewPeople = binding.viewPeople
        val tvDate = binding.date
        val tvTime = binding.tvTime
        val tvPeople = binding.tvPeople

        with(binding) {
            detailsName.text = restaurant.name
            address.text = restaurant.address
            // добавить картинку
        }
        openMapsClick(restaurant)

        // Обработка выбора даты
        viewDate.setOnClickListener {
            showMaterialDatePicker(tvDate)
        }

        // Обработка выбора времени
        viewTime.setOnClickListener {
            showTimePicker(tvTime)
        }

        // Обработка выбора количества гостей
        viewPeople.setOnClickListener {
            showPeoplePicker(tvPeople)
        }

        binding.frame8074.setOnClickListener {
            showAvailableTimeSlots()
        }

    }

    private fun showMaterialDatePicker(textView: TextView) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateInMillis }
            val months = arrayOf(
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"
            )
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            selectedDate = "$day ${months[month]} $year"
            textView.text = selectedDate
            animateWidthChange(binding.viewDate, ViewDimensions.DATE_EXPANDED_WIDTH)
        }

        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun showTimePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format(Locale("ru"), "%02d:%02d", selectedHour, selectedMinute)
                textView.text = selectedTime
                animateWidthChange(binding.viewTime, ViewDimensions.TIME_EXPANDED_WIDTH)
            },
            hour,
            minute,
            true
        ).apply {
            setTitle("Выберите время")
            setButton(DialogInterface.BUTTON_POSITIVE, "ОК", this)
            setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена", this)
        }

        timePickerDialog.show()
    }

    private fun showPeoplePicker(textView: TextView) {
        val items = arrayOf("1", "2", "3", "4", "5", "6", "7", "8")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите количество гостей")
        builder.setItems(items) { _, which ->
            selectedPeople = which + 1
            textView.text = selectedPeople.toString()
            animateWidthChange(binding.viewPeople, ViewDimensions.PEOPLE_EXPANDED_WIDTH)
        }
        builder.show()
    }
    private fun animateWidthChange(view: View, targetWidth: Int) {
        val anim = ValueAnimator.ofInt(view.width, targetWidth).apply {
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.width = value
                view.layoutParams = layoutParams
            }
            duration = 200 // milliseconds
        }
        anim.start()
    }

    private fun showAvailableTimeSlots() {
        val bottomSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.bottom_sheet_available_slots, null)

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(bottomSheetView)

        // Тестовые данные
        val timeSlots = listOf(
            TimeSlotAdapter.TimeSlot("15:00 - 16:00", "Столик в VIP-зоне (6 персон)", true),
            TimeSlotAdapter.TimeSlot("16:00 - 17:00", "Столик у бара (2 персоны)", true)
        )

        val adapter = TimeSlotAdapter(timeSlots) { selectedSlot ->
            val restaurantName = binding.detailsName.text.toString()
            val address = binding.address.text.toString()
            val date = binding.date.text.toString()
            val time = selectedSlot.time.split(" - ")[0]

            val newBooking = BookingItem(
                restaurantName,
                address,
                date,
                time,
                "Confirmed"
            )
            bookingsViewModel.addBooking(newBooking)

            binding.tvTime.text = selectedSlot.time
            dialog.dismiss()

            Toast.makeText(requireContext(), "Бронь добавлена в историю", Toast.LENGTH_SHORT).show()
        }

        bottomSheetView.findViewById<RecyclerView>(R.id.rv_time_slots).apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        bottomSheetView.findViewById<Button>(R.id.btn_close).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun openMapsClick(restaurant: Restaurant) {
        binding.tvOpenMaps.setOnClickListener {
            val openMaps = OpenMaps(requireContext())
            openMaps.showMapChooserDialog(restaurant.address)
        }
    }
}