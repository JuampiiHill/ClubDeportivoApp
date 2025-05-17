package com.example.clubdeportivo

import SocioItem
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MembersList : AppCompatActivity() {

    private lateinit var socioAdapter: SocioAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members_list)

        // Inicializar componentes
        searchView = findViewById(R.id.txt_search)
        recyclerView = findViewById(R.id.recyclerViewSocios)

        // Datos hardcodeados
        val socios = listOf(
            SocioItem("Hillcoat, Juan Pablo", "Paga"),
            SocioItem("Kalas, Jorge Adrián", "Paga"),
            SocioItem("Leone, Milena Nadin", "Paga"),
            SocioItem("Molina, María Julieta", "Paga"),
            SocioItem("Rodriguez, Carlos Douglas", "Paga"),
            SocioItem("Vega, José", "Vencida"),
            SocioItem("Zárate, Nicolás", "Vencida")
        )

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        socioAdapter = SocioAdapter(socios)
        recyclerView.adapter = socioAdapter

        // Navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            startActivity(Intent(this, ActivityList::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            startActivity(Intent(this, PayFee::class.java))
        }

        // Buscador (funcionalidad se puede agregar después)
        searchView.queryHint = "Buscar socio por DNI"
    }
}
