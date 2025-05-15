package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay)

        // Botonera inferior
        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnSocios).setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnActividades).setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnPagos).setOnClickListener {
            // Ya estás en pagos, no hace falta redirigir
        }

        // Redirigir al success_pay
        findViewById<Button>(R.id.btnPay).setOnClickListener {
            val intent = Intent(this, SuccessPayActivity::class.java)
            startActivity(intent)
        }
    }
}
