package com.example.clubdeportivo

import com.example.clubdeportivo.ActividadAdapter
import ActividadItem
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityList : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var actividades: List<ActividadItem>
    private lateinit var adapter: ActividadAdapter
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        dbHelper = UserDBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewActividades)
        searchView = findViewById(R.id.txt_search)
        btnAdd = findViewById(R.id.btn_add_activity)
        btnRemove = findViewById(R.id.btn_remove_activity)

        loadActivities()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtradas = actividades.filter {
                    it.nombre.contains(newText.orEmpty(), ignoreCase = true)
                }
                recyclerView.adapter = ActividadAdapter(filtradas) { actividad ->
                    val intent = Intent(this@ActivityList, AssignActivityActivity::class.java)
                    intent.putExtra("nombreActividad", actividad.nombre)
                    startActivity(intent)
                }
                return true
            }
        })

        btnAdd.setOnClickListener {
            val intent = Intent(this, NewActivityFormActivity::class.java)
            startActivity(intent)
        }

        btnRemove.setOnClickListener {
            val input = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Eliminar actividad")
                .setMessage("Nombre exacto de la actividad a eliminar:")
                .setView(input)
                .setPositiveButton("Eliminar") { _, _ ->
                    val nombre = input.text.toString()
                    val eliminado = dbHelper.eliminarActividadPorNombre(nombre)
                    if (eliminado) {
                        Toast.makeText(this, "Actividad eliminada", Toast.LENGTH_SHORT).show()
                        loadActivities()
                    } else {
                        Toast.makeText(this, "No se encontró la actividad", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.nav_btn_members).setOnClickListener {
            startActivity(Intent(this, MembersList::class.java))
        }
        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            startActivity(Intent(this, PayFee::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadActivities()
    }

    private fun loadActivities() {
        val actividadesDB = dbHelper.getAllActividades()
        actividades = actividadesDB.map {
            ActividadItem(it.nombre, it.cupoDisponible.toString())
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ActividadAdapter(actividades) { actividad ->
            val intent = Intent(this, AssignActivityActivity::class.java)
            intent.putExtra("nombreActividad", actividad.nombre)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
