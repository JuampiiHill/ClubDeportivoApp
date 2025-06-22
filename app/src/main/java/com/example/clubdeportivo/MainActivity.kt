package com.example.clubdeportivo

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //evento para que al hacer click en el botón nos lleve al login
        val button: Button = findViewById(R.id.btn_in)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerText = findViewById<TextView>(R.id.register)
        registerText.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("start_with_register", true)
            startActivity(intent)
        }

        //Función para darle color rojo al texto Registrate
        val fullText = registerText.text.toString()
        val palabraClave = "Registrate"
        val start = fullText.indexOf(palabraClave)

        if (start != -1) {
            val end = start + palabraClave.length
            val spannable = SpannableString(fullText)

            spannable.setSpan(
                ForegroundColorSpan("#B11719".toColorInt()),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            registerText.text = spannable
        }
    }
}