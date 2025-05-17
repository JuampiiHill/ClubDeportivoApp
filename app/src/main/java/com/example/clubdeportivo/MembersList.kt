package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MembersList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_members_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchView = findViewById<SearchView>(R.id.txt_search)
        searchView.setQuery("Buscar socio por DNI", false)


        //Eventos para el nav
        val btnNavHome: LinearLayout = findViewById(R.id.nav_btn_home)
        btnNavHome.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val btnNavMembers: LinearLayout = findViewById(R.id.nav_btn_members)
        btnNavMembers.setOnClickListener{
            val intent = Intent(this, MembersList::class.java)
            startActivity(intent)
        }

        val btnNavActivity: LinearLayout = findViewById(R.id.nav_btn_activities)
        btnNavActivity.setOnClickListener{
            val intent = Intent(this, ActivityList::class.java)
            startActivity(intent)
        }

        val btnNavPayments: LinearLayout = findViewById(R.id.nav_btn_payments)
        btnNavPayments.setOnClickListener{
            val intent = Intent(this, PayFee::class.java)
            startActivity(intent)
        }
    }
}