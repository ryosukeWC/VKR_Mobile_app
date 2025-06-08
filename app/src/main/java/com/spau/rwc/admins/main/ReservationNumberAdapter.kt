package com.spau.rwc.admins.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spau.rwc.R
import com.spau.rwc.model.NumberReservation

class ReservationNumberAdapter(
    private val reservations: List<NumberReservation>,
) : RecyclerView.Adapter<ReservationNumberAdapter.ReservationNumberViewHolder>() {

    inner class ReservationNumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.findViewById(R.id.tv_number)

//        init {
//            itemView.setOnClickListener {
//                onItemClick(reservations[adapterPosition])
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationNumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_reservation_number, parent, false)
        return ReservationNumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationNumberViewHolder, position: Int) {
        val item = reservations[position]
        holder.tvNumber.text = item.number
    }

    override fun getItemCount(): Int = reservations.size
}