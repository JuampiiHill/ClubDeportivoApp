import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clubdeportivo.R

class ActividadAdapter(private val actividades: List<ActividadItem>) :
    RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder>() {

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

        val color = if (actividad.cupoDisponible.equals("agotado", true)) {
            Color.parseColor("#C00000") // rojo
        } else {
            Color.parseColor("#3FA34D") // verde
        }
        holder.cupo.setTextColor(color)
    }

    override fun getItemCount() = actividades.size
}
