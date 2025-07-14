package com.example.clubdeportivo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate

class ActivityCarnet : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        val dbHelper = UserDBHelper(this)

        // RECIBIR DOCUMENTO DESDE INTENT
        val documento = intent.getStringExtra("documento")

        if (documento != null) {
            val member = dbHelper.getMemberByDocument(documento)
            val actividades = dbHelper.getActividadesDeSocio(documento)

            if (member != null) {
                findViewById<TextView>(R.id.nombreUsuario).text = "${member.name} ${member.surname}"
                findViewById<TextView>(R.id.dni).text = member.document
                findViewById<TextView>(R.id.generoUsuario).text = member.gender
                findViewById<TextView>(R.id.emailUsuario).text = member.email
                val vencimientoDate = LocalDate.parse(member.expirationDate)
                val hoy = LocalDate.now()
                val estadoCuotaTexto = if (vencimientoDate.isBefore(hoy)) "Vencida" else "Paga"
                findViewById<TextView>(R.id.statusQuote).text = estadoCuotaTexto
                findViewById<TextView>(R.id.vencimientoUsuario).text = member.expirationDate

                val btnPagar = findViewById<Button>(R.id.btn_add)
                if (member.pay) {
                    btnPagar.visibility = View.GONE
                } else {
                    btnPagar.visibility = View.VISIBLE
                    btnPagar.setOnClickListener {
                        val intent = Intent(this, PayFee::class.java).apply {
                            putExtra("documento", member.document)
                            putExtra("nombre", member.name)
                            putExtra("apellido", member.surname)
                            putExtra("email", member.email)
                        }
                        startActivity(intent)
                    }
                }

                val actividadesTexto = if (actividades.isNotEmpty()) {
                    actividades.joinToString(separator = "\n") { "- $it" }
                } else {
                    "Este socio no tiene actividades asignadas."
                }

                val actividadesTextView = findViewById<TextView>(R.id.txt_actividades_socio)
                actividadesTextView.text = actividadesTexto

            } else {
                Log.e("CARNET", "No se encontró socio con documento $documento")
                Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Log.e("CARNET", "Documento no recibido")
            finish()
        }

        findViewById<Button>(R.id.btn_back).setOnClickListener {
            finish()
        }
    }
}
