package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAddMember = findViewById<LinearLayout>(R.id.btn_add_member)
        btnAddMember.setOnClickListener{
            val intent = Intent(this, NewMember::class.java)
            startActivity(intent)
        }

        val btnPay: LinearLayout = findViewById(R.id.btn_pay)
        btnPay.setOnClickListener{
            val intent = Intent(this, PayFee::class.java)
            startActivity(intent)
        }

        val btListQuotas: LinearLayout = findViewById(R.id.btn_list_quotas)
        btListQuotas.setOnClickListener{
            val intent = Intent(this, MembersList::class.java)
            startActivity(intent)
        }

        val btnAddActivity: LinearLayout = findViewById(R.id.btn_add_activity)
        btnAddActivity.setOnClickListener{
            val intent = Intent(this, ActivityList::class.java)
            startActivity(intent)
        }

        //Eventos para el nav

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