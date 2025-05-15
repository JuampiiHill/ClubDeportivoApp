package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_list)

        // Configuración del RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.activityRecycler)

        val activities = listOf(
            ActivityItem("CrossFit", "5", true),
            ActivityItem("Fútbol 7", "agotado", false),
            ActivityItem("Funcional", "2", true),
            ActivityItem("Musculación", "2", true),
            ActivityItem("Natación", "agotado", false)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ActivityAdapter(activities)

        // Configuración de los botones del menú inferior
        val btnInicio = findViewById<ImageButton>(R.id.btnInicio)
        val btnSocios = findViewById<ImageButton>(R.id.btnSocios)
        val btnActividades = findViewById<ImageButton>(R.id.btnActividades)
        val btnPagos = findViewById<ImageButton>(R.id.btnPagos)

        btnInicio.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        btnSocios.setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        btnActividades.setOnClickListener {
            // no se navega
        }

        btnPagos.setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }
    }
}
