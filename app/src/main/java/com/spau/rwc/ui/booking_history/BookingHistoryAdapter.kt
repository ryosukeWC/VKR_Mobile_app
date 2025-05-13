package com.spau.rwc.ui.booking_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spau.rwc.R
import com.spau.rwc.model.BookingItem

class BookingHistoryAdapter(
    private val bookingItems: MutableList<BookingItem>,
    private val onItemClick: (BookingItem) -> Unit
) : RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder>() {

    inner class BookingHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restaurantName: TextView = itemView.findViewById(R.id.name)
        val address: TextView = itemView.findViewById(R.id.address)
//        val bookingDate: TextView = itemView.findViewById(R.id.booking_date)
////        val status: TextView = itemView.findViewById(R.id.booking_status)
////        val checkButton: Button = itemView.findViewById(R.id.check_button)
//        val checkAvailabilityButton: Button = itemView.findViewById(R.id.check_availability_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return BookingHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingHistoryViewHolder, position: Int) {
        val item = bookingItems[position]
        holder.restaurantName.text = item.restaurantName
        holder.address.text = "${item.address}\nДата: ${item.bookingDate}\nВремя: ${item.bookingTime}"
    }

    override fun getItemCount(): Int = bookingItems.size
}