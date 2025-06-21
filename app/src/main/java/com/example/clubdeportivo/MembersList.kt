package com.example.clubdeportivo

import SocioAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

class MembersList : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var socios: List<Socio>
    private lateinit var adapter: SocioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members_list)

        dbHelper = UserDBHelper(this)
        listView = findViewById(R.id.listViewSocios)
        searchView = findViewById(R.id.txt_search)

        socios = dbHelper.getAllSocios()
        Log.d("SOCIOS", "Cantidad: ${socios.size}")

        adapter = SocioAdapter(this, socios)
        listView.adapter = adapter

        // Navegación al carnet al hacer clic en un socio
        listView.setOnItemClickListener { _, _, position, _ ->
            val socio = socios[position]
            val intent = Intent(this, ActivityCarnet::class.java)
            intent.putExtra("documento", socio.documento)
            startActivity(intent)
        }

        // Filtro de búsqueda por apellido
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtrados = socios.filter {
                    it.apellido.contains(newText.orEmpty(), ignoreCase = true)
                }
                listView.adapter = SocioAdapter(this@MembersList, filtrados)
                return true
            }
        })



        // Barra de navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            startActivity(Intent(this, ActivityList::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            startActivity(Intent(this, PayFee::class.java))
        }
    }
}

