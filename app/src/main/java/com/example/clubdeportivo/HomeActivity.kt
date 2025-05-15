package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // 🔴 INSCRIBIR NUEVO SOCIO
        val btnAddMember = findViewById<ImageButton>(R.id.imageButton8)
        btnAddMember.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 🟡 LISTAR SOCIOS
        val btnListMembers = findViewById<ImageButton>(R.id.imageButton9)
        btnListMembers.setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        // 🟢 LISTAR ACTIVIDADES
        val btnActivities = findViewById<ImageButton>(R.id.imageButton10)
        btnActivities.setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        // 🔵 ESTADO DE CUOTAS (PAGOS)
        val btnPayStatus = findViewById<ImageButton>(R.id.imageButton7)
        btnPayStatus.setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }

        // NAV BAR INFERIOR

        // 🔴 HOME (no hace nada porque ya estás ahí)
        val navHome = findViewById<ImageButton>(R.id.imageButton12)
        navHome.setOnClickListener {
            // No redirige
        }

        // 🟡 SOCIOS (va a lista de socios)
        val navMembers = findViewById<ImageButton>(R.id.imageButton13)
        navMembers.setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        // 🟢 ACTIVIDADES
        val navActivities = findViewById<ImageButton>(R.id.imageButton14)
        navActivities.setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        // 🔵 PAGOS
        val navPayments = findViewById<ImageButton>(R.id.imageButton15)
        navPayments.setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }

        }
    }

