package com.example.clubdeportivo

import ActividadAdapter
import ActividadItem
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActividades)

        val actividades = listOf(
            ActividadItem("CrossFit", "5"),
            ActividadItem("Futbol 7", "agotado"),
            ActividadItem("Funcional", "2"),
            ActividadItem("Musculación", "2"),
            ActividadItem("Natación", "agotado")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ActividadAdapter(actividades)

        //Eventos para el nav (no se toca)
        val btnNavHome = findViewById<LinearLayout>(R.id.nav_btn_home)
        btnNavHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val btnNavMembers = findViewById<LinearLayout>(R.id.nav_btn_members)
        btnNavMembers.setOnClickListener {
            val intent = Intent(this, MembersList::class.java)
            startActivity(intent)
        }

        val btnNavActivity: LinearLayout = findViewById(R.id.nav_btn_activities)
        btnNavActivity.setOnClickListener {
            val intent = Intent(this, ActivityList::class.java)
            startActivity(intent)
        }

        val btnNavPayments = findViewById<LinearLayout>(R.id.nav_btn_payments)
        btnNavPayments.setOnClickListener {
            val intent = Intent(this, PayFee::class.java)
            startActivity(intent)
        }
    }
}
