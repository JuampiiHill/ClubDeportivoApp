package com.example.clubdeportivo

import ActividadItem
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ActividadAdapter(
    private val actividades: List<ActividadItem>,
    private val onClick: (ActividadItem) -> Unit
) : RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

    class ActividadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreActividad)
        val cupo: TextView = view.findViewById(R.id.cupoDisponible)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item, parent, false)
        return ActividadViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActividadViewHolder, position: Int) {
        val actividad = actividades[position]
        holder.nombre.text = actividad.nombre
        holder.cupo.text = "Cupo disponible: ${actividad.cupoDisponible}"

        val cupoStr = actividad.cupoDisponible.trim().lowercase()
        val color = if (cupoStr == "agotado" || cupoStr == "0") {
            Color.parseColor("#C00000")
        } else {
            Color.parseColor("#3FA34D")
        }
        holder.cupo.setTextColor(color)

        holder.itemView.setOnClickListener {
            if (cupoStr != "agotado" && cupoStr != "0") {
                onClick(actividad)
            }
        }
    }

    override fun getItemCount() = actividades.size
}
