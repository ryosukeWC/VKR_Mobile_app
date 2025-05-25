package com.spau.rwc.ui.booking_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spau.rwc.R
import com.spau.rwc.model.Reservation

class BookingHistoryAdapter(
    private val bookingItems: MutableList<Reservation>,
    private val onItemClick: (Reservation) -> Unit
) : RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder>() {

    inner class BookingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantName: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
        val date: TextView = itemView.findViewById(R.id.date)
        val people: TextView = itemView.findViewById(R.id.tv_people)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingHistoryViewHolder, position: Int) {
        val item = bookingItems[position]
        holder.restaurantName.text = "Тест" // вытащить из ресторана
        holder.date.text = "${item.date} ${item.time}"
        holder.people.text = item.guests.toString()
        holder.address.text = "Ярославль" // вытащить из ресторана
    }

    override fun getItemCount(): Int = bookingItems.size

    fun update(newItems: List<Reservation>) {
        bookingItems.clear()
        bookingItems.addAll(newItems)
        notifyDataSetChanged()
    }
}