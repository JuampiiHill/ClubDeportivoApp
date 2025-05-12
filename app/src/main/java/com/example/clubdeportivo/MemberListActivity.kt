package com.example.clubdeportivo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.content.Intent



class MemberListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_list)

        findViewById<ImageButton>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnSocios).setOnClickListener {

        }

        findViewById<ImageButton>(R.id.btnActividades).setOnClickListener {
            startActivity(Intent(this, ActivityListActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnPagos).setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }
    }
}
