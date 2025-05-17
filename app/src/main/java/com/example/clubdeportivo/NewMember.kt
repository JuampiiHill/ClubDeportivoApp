package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewMember : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_member)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegister: Button = findViewById(R.id.btn_large_in)
        btnRegister.setOnClickListener {
            val intent = Intent(this, SuccessRegister::class.java)
            startActivity(intent)
        }

        val btnBack: Button = findViewById(R.id.btn_back)
        btnBack.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        //Eventos para el nav
        val btnNavHome: LinearLayout = findViewById(R.id.nav_btn_home)
        btnNavHome.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
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