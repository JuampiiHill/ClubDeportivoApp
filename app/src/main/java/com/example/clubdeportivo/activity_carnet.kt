package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityCarnet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carnet)

        // Obtener datos del intent
        val nombre = intent.getStringExtra("nombre") ?: "Desconocido"
        val estado = intent.getStringExtra("estado") ?: "Desconocido"

        // Mostrar en los TextView
        findViewById<TextView>(R.id.nombreUsuario).text = nombre
        findViewById<TextView>(R.id.estadoCuota).text = "Estado de cuota: $estado"

        // Botón Pagar
        val btnPay = findViewById<Button>(R.id.btn_add)
        btnPay.setOnClickListener {
            val intent = Intent(this, PayFee::class.java)
            startActivity(intent)
        }

        // Botón Volver
        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
