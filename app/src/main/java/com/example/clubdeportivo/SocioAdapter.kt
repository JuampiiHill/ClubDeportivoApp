package com.example.clubdeportivo

import SocioItem
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SocioAdapter(private val socios: List<SocioItem>) : RecyclerView.Adapter<SocioAdapter.SocioViewHolder>() {

    class SocioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.nombreSocio)
        val estadoCuota: TextView = view.findViewById(R.id.estadoCuotaSocio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_socio_item, parent, false)
        return SocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        val socio = socios[position]
        holder.nombre.text = socio.nombre
        holder.estadoCuota.text = "Estado de cuota: ${socio.estadoCuota}"

        val color = if (socio.estadoCuota.equals("Paga", true)) {
            Color.parseColor("#3FA34D") // verde
        } else {
            Color.parseColor("#C00000") // rojo
        }
        holder.estadoCuota.setTextColor(color)
    }

    override fun getItemCount() = socios.size
}
