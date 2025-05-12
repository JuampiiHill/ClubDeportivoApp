package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        // Botones del menú superior
        findViewById<ImageButton>(R.id.btnNuevoSocio).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnAbonar).setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnEstado).setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnAsignar).setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        // Botonera inferior
        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            // Ya estás en Home, podés dejarlo vacío o hacer un refresh
        }

        findViewById<ImageButton>(R.id.btnSocios).setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnActividades).setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnPagos).setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }
    }
}
