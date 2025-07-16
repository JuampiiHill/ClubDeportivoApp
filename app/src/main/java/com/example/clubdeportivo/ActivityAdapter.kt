package com.example.clubdeportivo

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(
    private var activities: List<Activity>,
    private val onClick: (Activity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.activity_name)
        val day: TextView = view.findViewById(R.id.day)
        val schedule: TextView = view.findViewById(R.id.txt_activity_schedule)
        val cupo: TextView = view.findViewById(R.id.availableSpace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.name.text = activity.name
        holder.day.text = "Día: ${activity.day}"
        holder.schedule.text = "Horario: ${activity.schedule}"
        holder.cupo.text = "Cupo disponible: ${activity.availableSpace}"

        val color = if (activity.availableSpace <= 0) {
            Color.parseColor("#C00000")
        } else {
            Color.parseColor("#3FA34D")
        }
        holder.cupo.setTextColor(color)

        holder.itemView.setOnClickListener {
            if (activity.availableSpace > 0) {
                onClick(activity)
            }
        }
    }

    override fun getItemCount() = activities.size

    fun updateList(newList: List<Activity>) {
        activities = newList
        notifyDataSetChanged()
    }
}