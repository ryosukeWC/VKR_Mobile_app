package com.spau.rwc.ui.restaurnat_details.timeslots

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spau.rwc.R

class TimeSlotAdapter(
    private val timeSlots: List<TimeSlot>,
    private val onItemClick: (TimeSlot) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>() {

    data class TimeSlot(
        val time: String,
        val tableInfo: String,
        val isAvailable: Boolean
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeSlot: TextView = itemView.findViewById(R.id.tv_time_slot)
        val tableInfo: TextView = itemView.findViewById(R.id.tv_table_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = timeSlots[position]
        holder.timeSlot.text = item.time
        holder.tableInfo.text = item.tableInfo

        if (!item.isAvailable) {
            holder.timeSlot.setTextColor(Color.RED)
            holder.tableInfo.setTextColor(Color.RED)
        } else {
            holder.timeSlot.setTextColor(Color.BLACK)
            holder.tableInfo.setTextColor(Color.DKGRAY)
        }

        holder.itemView.setOnClickListener {
            if (item.isAvailable) {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount() = timeSlots.size
}