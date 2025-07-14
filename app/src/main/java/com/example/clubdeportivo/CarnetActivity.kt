package com.example.clubdeportivo

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Locale

class CarnetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_carnet2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val document = intent.getStringExtra("document")

        if (document == null) {
            Toast.makeText(this, "Documento no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = UserDBHelper(this)
        val member = dbHelper.getMemberByDocument(document)

        if (member == null) {
            Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val fullName = findViewById<TextView>(R.id.fullNameUser)
        val documentText = findViewById<TextView>(R.id.document)
        val gender = findViewById<TextView>(R.id.gender)
        val email = findViewById<TextView>(R.id.email)

        fullName.text = getString(R.string.member_fullname, member.name, member.surname)
        documentText.text = member.document
        gender.text = member.gender
        email.text = member.email

        val btnPay = findViewById<Button>(R.id.btn_pay)

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val todayDate = formatter.parse(formatter.format(calendar.time))

        val statusQuoteText = if (member.expirationDate.isNullOrEmpty()) {
            "No paga"
        } else {
            try {
                val expirationDate = formatter.parse((member.expirationDate))
                if (expirationDate.before(todayDate)) "Vencida" else "Paga"
            } catch (e: Exception) {
                Log.e("CARNET", "Error al parsear fecha de vencimiento: ${member.expirationDate}", e)
                "Fecha inválida"
            }
        }
        findViewById<TextView>(R.id.statusQuote).text = statusQuoteText

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val formattedExpiration = if (member.expirationDate.isNullOrEmpty()) {
            "No paga"
        } else {
            try {
                val date = inputFormat.parse(member.expirationDate)
                formatter.format(date!!)
            } catch (e: Exception) {
                member.expirationDate
            }
        }

        val formattedBirthDate = try {
            val date = inputFormat.parse(member.dateOfBirth)
            formatter.format(date!!)
        } catch (e: Exception) {
            member.dateOfBirth
        }


        findViewById<TextView>(R.id.expirationDate).text = formattedExpiration
        findViewById<TextView>(R.id.dateOfBirth).text = formattedBirthDate

        if (statusQuoteText == "No paga" || statusQuoteText == "Vencida") {
            btnPay.visibility = View.VISIBLE
            btnPay.setOnClickListener{
                val intent = Intent(this, PayFee::class.java)
                intent.putExtra("document", member.document)
                startActivity(intent)
                Log.e("PAY_FEE", "Enviando documento hacia la pagina de pago ${member.document}" )
                // ACA DEBEMOS DERIVAR AL PAGO DE LA CUOTA ENVIANDO EL DOCUMENTO COMO UN EXTRA PARA TRAER LOS DATOS
                // DEL SOCIO DESDE LA DB
            }
        }

        findViewById<Button>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }
}