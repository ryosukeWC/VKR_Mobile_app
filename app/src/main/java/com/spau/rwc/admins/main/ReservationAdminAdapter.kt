package com.spau.rwc.admins.main

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spau.rwc.R
import com.spau.rwc.databinding.ItemAdminReservationBinding
import com.spau.rwc.model.ReservationItem
import kotlin.random.Random

class ReservationAdminAdapter(
    private var reservations: List<ReservationItem>
) : RecyclerView.Adapter<ReservationAdminAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(private val binding: ItemAdminReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReservationItem) {
            with(binding) {
                // Установка данных
                tvReservationNumber.text = item.reservationNumber
                someIdTv.text = item.userInitial
                tvDateTime.text = item.dateTime
                tvTableNumber.text = item.tableNumber
                tvPeopleCount.text = item.peopleCount

                avatarImage.backgroundTintList = ColorStateList.valueOf(getRandomColor())

                // Изначальная настройка видимости кнопок
                updateButtonVisibility(item.status)

                // Обработчики кликов
                acceptButton.setOnClickListener {
                    showConfirmationDialog(
                        "Подтвердить бронь?",
                        "Вы уверены, что хотите подтвердить бронь ${item.reservationNumber}?",
                        true,
                        item
                    )
                }

                cancelButton.setOnClickListener {
                    showConfirmationDialog(
                        "Отклонить бронь?",
                        "Вы уверены, что хотите отклонить бронь ${item.reservationNumber}?",
                        false,
                        item
                    )
                }
            }
        }

        private fun updateButtonVisibility(status: String) {
            with(binding) {
                when (status) {
                    "Ждет подтверждения" -> {
                        cancelButton.visibility = View.VISIBLE
                        acceptButton.visibility = View.VISIBLE
                        otklButton.visibility = View.GONE
                        potvButton.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }
                    "Отклонено" -> {
                        cancelButton.visibility = View.GONE
                        acceptButton.visibility = View.GONE
                        otklButton.visibility = View.VISIBLE
                        potvButton.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }
                    "Подтверждено" -> {
                        cancelButton.visibility = View.GONE
                        acceptButton.visibility = View.GONE
                        otklButton.visibility = View.GONE
                        potvButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }

        private fun showConfirmationDialog(
            title: String,
            message: String,
            isAccept: Boolean,
            item: ReservationItem
        ) {
            AlertDialog.Builder(binding.root.context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Да") { dialog, _ ->
                    dialog.dismiss()
                    showProgressAndUpdateStatus(isAccept, item)
                }
                .setNegativeButton("Нет") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        private fun showProgressAndUpdateStatus(isAccept: Boolean, item: ReservationItem) {
            with(binding) {
                // Показываем ProgressBar и скрываем кнопки
                progressBar.visibility = View.VISIBLE
                cancelButton.visibility = View.GONE
                acceptButton.visibility = View.GONE

                // Запускаем задержку для имитации обработки
                binding.root.postDelayed({
                    progressBar.visibility = View.GONE

                    // Обновляем статус (в реальном приложении здесь должен быть вызов API)
                    val newStatus = if (isAccept) "Подтверждено" else "Отклонено"

                    // В реальном приложении нужно обновить данные в списке и уведомить адаптер
                    // Для примера просто меняем видимость кнопок
                    updateButtonVisibility(newStatus)

                    // Здесь можно добавить вызов callback для обновления данных в Activity/Fragment
                }, 3000) // 3 секунды задержки
            }
        }
        private fun getRandomColor(): Int {
            // Генерируем случайные значения RGB
            val r = Random.nextInt(256)
            val g = Random.nextInt(256)
            val b = Random.nextInt(256)

            // Создаем цвет с небольшой прозрачностью (0xAARRGGBB)
            return Color.argb(255, r, g, b)

            // Или полностью случайный цвет:
            // return Color.rgb(r, g, b)
        }

    }

    fun updateData(newData: List<ReservationItem>) {
        reservations = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = ItemAdminReservationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    override fun getItemCount() = reservations.size
}