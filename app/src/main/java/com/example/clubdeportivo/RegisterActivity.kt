package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputDocument = findViewById<EditText>(R.id.inputDocument)
        val inputName = findViewById<EditText>(R.id.inputName)
        val inputLastName = findViewById<EditText>(R.id.inputLastName)
        val inputEmail = findViewById<EditText>(R.id.inputEmail)
        val btnRegister = findViewById<Button>(R.id.btnRegisterUser)

        btnRegister.setOnClickListener {
            val name = inputName.text.toString()
            val lastName = inputLastName.text.toString()

            if (name.isNotEmpty() && lastName.isNotEmpty()) {
                val intent = Intent(this, SuccessRegister::class.java)
                startActivity(intent)
            } else {
                // Keep the warning for incomplete fields
                Toast.makeText(this, "Por favor completá todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
