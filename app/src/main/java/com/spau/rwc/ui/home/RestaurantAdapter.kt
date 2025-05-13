package com.spau.rwc.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.spau.rwc.R
import com.spau.rwc.model.Restaurant

class RestaurantAdapter : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var restaurants = emptyList<Restaurant>()

    private var onItemClick: ((Restaurant) -> Unit)? = null

    fun submitList(newList: List<Restaurant>) {
        restaurants = newList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Restaurant) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: Restaurant) {

            itemView.apply {
                findViewById<TextView>(R.id.name).text = restaurant.name

                findViewById<ImageView>(R.id.image).load(restaurant.logoUrl)

                setOnClickListener { onItemClick?.invoke(restaurant) }
            }
        }
    }
}