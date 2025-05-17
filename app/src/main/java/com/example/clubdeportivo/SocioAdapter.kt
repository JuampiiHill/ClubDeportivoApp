package com.example.clubdeportivo

import SocioItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SocioAdapter(
    private val socios: List<SocioItem>,
    private val onItemClick: (SocioItem) -> Unit
) : RecyclerView.Adapter<SocioAdapter.SocioViewHolder>() {

    class SocioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombreSocio)
        val estado: TextView = itemView.findViewById(R.id.estadoCuotaSocio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_socio_item, parent, false)
        return SocioViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        val socio = socios[position]
        holder.nombre.text = socio.nombre
        holder.estado.text = "Estado de cuota: ${socio.estadoCuota}"

        holder.itemView.setOnClickListener {
            onItemClick(socio)
        }
    }

    override fun getItemCount(): Int = socios.size
}
