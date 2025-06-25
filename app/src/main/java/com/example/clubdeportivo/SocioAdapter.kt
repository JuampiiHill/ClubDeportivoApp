import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.toColorInt
import com.example.clubdeportivo.R
import com.example.clubdeportivo.Socio
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SocioAdapter(private val context: Context, private val socios: List<Socio>) : BaseAdapter() {

    override fun getCount(): Int = socios.size
    override fun getItem(position: Int): Any = socios[position]
    override fun getItemId(position: Int): Long = socios[position].id.toLong()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_socio_item, parent, false)
        val socio = socios[position]

        val nombreTextView = view.findViewById<TextView>(R.id.nombreSocio)
        val estadoTextView = view.findViewById<TextView>(R.id.estadoCuotaSocio)

        nombreTextView.text = "${socio.apellido}, ${socio.nombre}"

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val today = LocalDate.now()
        val expirationDate = LocalDate.parse(socio.vencimiento,formatter)

        val isExpirate = today.isAfter(expirationDate)

        if(isExpirate || !socio.pago) {
            estadoTextView.text = "Estado de cuota: Vencida"
            estadoTextView.setTextColor("#e74c3c".toColorInt())
        } else {
            estadoTextView.text = "Estado de cuota: Paga"
            estadoTextView.setTextColor("#27ae60".toColorInt())
        }
        return view
    }
}
