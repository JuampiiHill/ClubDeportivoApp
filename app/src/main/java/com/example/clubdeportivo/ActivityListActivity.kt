package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ActivityListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_list)

        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnSocios).setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnActividades).setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnPagos).setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }
}}