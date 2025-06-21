import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.clubdeportivo.R
import com.example.clubdeportivo.Socio

class SocioAdapter(private val context: Context, private val socios: List<Socio>) : BaseAdapter() {

    override fun getCount(): Int = socios.size
    override fun getItem(position: Int): Any = socios[position]
    override fun getItemId(position: Int): Long = socios[position].id.toLong()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_socio_item, parent, false)
        val socio = socios[position]

        val nombreTextView = view.findViewById<TextView>(R.id.nombreSocio)
        val estadoTextView = view.findViewById<TextView>(R.id.estadoCuotaSocio)

        nombreTextView.text = "${socio.apellido}, ${socio.nombre}"

        // Validación por vencimiento
        val hoy = java.time.LocalDate.now()
        val fechaVencimiento = java.time.LocalDate.parse(socio.vencimiento)

        val estaVencido = hoy.isAfter(fechaVencimiento)

        if (estaVencido || !socio.pago) {
            estadoTextView.text = "Estado de cuota: Vencida"
            estadoTextView.setTextColor(android.graphics.Color.parseColor("#e74c3c"))
        } else {
            estadoTextView.text = "Estado de cuota: Paga"
            estadoTextView.setTextColor(android.graphics.Color.parseColor("#27ae60"))
        }

        return view
    }

}
