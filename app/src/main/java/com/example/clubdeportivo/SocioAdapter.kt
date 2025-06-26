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
import com.example.clubdeportivo.Member
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SocioAdapter(private val context: Context, private val members: List<Member>) : BaseAdapter() {

    override fun getCount(): Int = members.size
    override fun getItem(position: Int): Any = members[position]
    override fun getItemId(position: Int): Long = members[position].id.toLong()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_socio_item, parent, false)
        val member = members[position]

        val nombreTextView = view.findViewById<TextView>(R.id.nombreSocio)
        val estadoTextView = view.findViewById<TextView>(R.id.estadoCuotaSocio)

        nombreTextView.text = "${member.surname}, ${member.name}"

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val today = LocalDate.now()
        val expirationDate = LocalDate.parse(member.expirationDate,formatter)

        val isExpirate = today.isAfter(expirationDate)

        if(isExpirate || !member.pay) {
            estadoTextView.text = "Estado de cuota: Vencida"
            estadoTextView.setTextColor("#e74c3c".toColorInt())
        } else {
            estadoTextView.text = "Estado de cuota: Paga"
            estadoTextView.setTextColor("#27ae60".toColorInt())
        }
        return view
    }
}
