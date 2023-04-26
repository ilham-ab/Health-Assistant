package com.example.health_ass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DayAdapter(private val days: List<Day>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.day_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        holder.dayTextView.text = day.dayOfMonth.toString()
        if (day.isCurrentDay) {
            holder.dayTextView.setBackgroundResource(R.drawable.current_day_background)
        } else {
            holder.dayTextView.background = null
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }
}
