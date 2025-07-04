package com.spau.rwc.ui.booking_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spau.rwc.R
import com.spau.rwc.model.Reservation
import com.spau.rwc.model.Restaurant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class BookingHistoryAdapter(
    private var bookingItems: List<Reservation>,
    private var restaurantMap: Map<Int, Restaurant> = emptyMap(),
    private val onItemClick: (Reservation) -> Unit
) : RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder>() {

    inner class BookingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantName: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
        val date: TextView = itemView.findViewById(R.id.date)
        val people: TextView = itemView.findViewById(R.id.tv_people)
        val status: TextView = itemView.findViewById(R.id.status)
        val bookButton: TextView = itemView.findViewById(R.id.book_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingHistoryViewHolder, position: Int) {
        val item = bookingItems[position]
        val restaurant = restaurantMap[item.restaurantId]

        holder.restaurantName.text = restaurant?.name ?: "Неизвестный ресторан"
        holder.address.text = restaurant?.address ?: "Адрес не найден"
        holder.date.text = "${item.date} ${item.time}"
        holder.people.text = item.guests.toString()
        holder.status.text = item.status

        val context = holder.itemView.context
        val statusLower = item.status.lowercase()
        val today = LocalDate.now()

        // Установка цвета статуса
        when (statusLower) {
            "подтверждено" -> holder.status.setTextColor(context.getColor(R.color.status_confirmed))
            "ждет подтверждения" -> holder.status.setTextColor(context.getColor(R.color.status_pending))
            "отклонено" -> holder.status.setTextColor(context.getColor(R.color.status_declined))
            else -> holder.status.setTextColor(context.getColor(R.color.black))
        }

        // Логика показа кнопки
        val shouldShowButton = (statusLower == "подтверждено" || statusLower == "ждет подтверждения") &&
                item.date >= today

        holder.bookButton.visibility = if (shouldShowButton) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener { onItemClick(item) }
    }


    override fun getItemCount(): Int = bookingItems.size

    fun updateReservations(newItems: List<Reservation>) {
        bookingItems = newItems.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

    fun updateRestaurants(newMap: Map<Int, Restaurant>) {
        restaurantMap = newMap
        notifyDataSetChanged()
    }
}
