package com.example.clubdeportivo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(private val activities: List<ActivityItem>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_member, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.name.text = activity.name
        holder.slots.text = if (activity.isAvailable)
            "Cupo disponible: ${activity.availableSlots}"
        else
            "Cupo disponible: ${activity.availableSlots}"

        holder.slots.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (activity.isAvailable) R.color.green else R.color.red
            )
        )
    }

    override fun getItemCount(): Int = activities.size

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.memberName)
        val slots: TextView = view.findViewById(R.id.memberStatus)
    }
}
