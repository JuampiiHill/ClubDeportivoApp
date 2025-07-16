package com.example.clubdeportivo

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActivityList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val searchView = findViewById<SearchView>(R.id.box_search)
        searchView.post {
            val searchText = searchView.findViewById<EditText>(
                searchView.context.resources.getIdentifier(
                    "search_src_text","id", "android"
                )
            )
            searchText?.let {
                it.setTextColor("#A7A8A9".toColorInt())
                it.setHintTextColor("#A7A8A9".toColorInt())
                it.typeface = ResourcesCompat.getFont(this, R.font.nunito)
            }

            val searchIcon = searchView.findViewById<ImageView>(
                searchView.context.resources.getIdentifier(
                    "search_mag_icon", "id", "android"
                )
            )
            searchIcon?.setColorFilter("#A7A8A9".toColorInt(), android.graphics.PorterDuff.Mode.SRC_IN)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActividades)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = UserDBHelper(this)
        val activities = dbHelper.getAllActividades()

        val adapter = ActivityAdapter(activities) { activity ->
            Toast.makeText(this, "Seleccionaste", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        //Filtrado de actividades
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = activities.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                adapter.updateList(filtered)
                return true
            }
        })

    }

    /**private lateinit var dbHelper: UserDBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var actividades: List<ActividadItem>
    private lateinit var adapter: ActividadAdapter
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button



        dbHelper = UserDBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewActividades)
        searchView = findViewById(R.id.box_search)
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
    } **/
}
