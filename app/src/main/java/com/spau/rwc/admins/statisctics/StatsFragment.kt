package com.spau.rwc.admins.statisctics

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.spau.rwc.R
import com.spau.rwc.databinding.FragmentStatsBinding
import kotlin.random.Random

class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(Color.parseColor("#212429")) // Фон всей страницы
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHourlyChart()
        setupHeatmap()
    }

    private fun setupHourlyChart() {
        val hours = (10..22).toList()
        val bookings = listOf(5, 8, 10, 12, 15, 20, 25, 30, 28, 25, 20, 15, 10)

        val entries = hours.mapIndexed { index, hour ->
            BarEntry(hour.toFloat(), bookings[index].toFloat())
        }

        val dataSet = BarDataSet(entries, "Бронирования").apply {
            color = Color.parseColor("#F9690F")
            valueTextColor = Color.WHITE
            valueTextSize = 10f
        }

        binding.hourlyChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                textColor = Color.WHITE
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}:00"
                    }
                }
            }
            axisLeft.apply {
                axisMinimum = 0f
                textColor = Color.WHITE
            }
            axisRight.isEnabled = false
            legend.isEnabled = false
            setBackgroundColor(Color.parseColor("#212429"))
            animateY(1000)
            setFitBars(true)
        }
    }

    private fun setupHeatmap() {
        val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
        val hours = (10..22).toList()
        val headers = listOf("День/Час") + hours.map { "$it:00" }

        val data = daysOfWeek.map {
            hours.map { Random.nextInt(0, 100) }
        }

        binding.heatmapGrid.numColumns = headers.size
        binding.heatmapGrid.adapter = object : BaseAdapter() {
            override fun getCount(): Int = daysOfWeek.size * headers.size
            override fun getItem(position: Int): Any = position
            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_heatmap_cell, parent, false)

                val textView = view.findViewById<TextView>(R.id.heatmapCell)

                val isHeaderRow = position < headers.size
                val isHeaderColumn = position % headers.size == 0

                when {
                    isHeaderRow -> {
                        textView.text = headers[position]
                        textView.setBackgroundColor(Color.TRANSPARENT)
                        textView.setTextColor(Color.WHITE)
                    }
                    isHeaderColumn -> {
                        val dayIndex = position / headers.size
                        textView.text = daysOfWeek[dayIndex]
                        textView.setBackgroundColor(Color.TRANSPARENT)
                        textView.setTextColor(Color.WHITE)
                    }
                    else -> {
                        val dayIndex = position / headers.size
                        val hourIndex = position % headers.size - 1
                        val value = data[dayIndex][hourIndex]

                        textView.text = "$value%"

                        val backgroundColor = when {
                            value < 30 -> Color.parseColor("#4CAF50")
                            value < 70 -> Color.parseColor("#FFC107")
                            else -> Color.parseColor("#F44336")
                        }

                        textView.setBackgroundColor(backgroundColor)
                        textView.setTextColor(if (value > 50) Color.WHITE else Color.BLACK)
                    }
                }

                return view
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
