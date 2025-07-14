package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class MembersList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members_list)

        val userName = intent.getStringExtra("nombre_usuario")

        //Cambiar color del texto del recuadro de búsqueda
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

        // Cargar socios desde la base de datos
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = UserDBHelper(this)
        val members = dbHelper.getAllMembers()

        val adapter = MemberAdapter(members)
        recyclerView.adapter = adapter

        //Filtrado de socios
        val allMembers = members.toList()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = allMembers.filter { member ->
                    member.surname.contains(newText.orEmpty(), ignoreCase = true)
                }
                adapter.updateList(filteredList)
                return true
            }
        })

        // Barra de navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            val intent = Intent(this, ActivityList::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            val intent = Intent(this, PayFee::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }
    }
}