package com.example.clubdeportivo

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_actitivy)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // CÓDIGO PARA MOSTRAR DIFERENTES FORMULARIOS DEPENDIENDO QUE BOTÓN SE PRESIONE
        val btnIn: Button = findViewById(R.id.btn_in)
        val btnRegister: Button = findViewById(R.id.btn_register)
        val layoutRegister = findViewById<LinearLayout>(R.id.register_container)
        val layoutLogin = findViewById<LinearLayout>(R.id.login_container)

        fun toggleButtons(isRegisterActive: Boolean) {
            val paramsRegister = btnRegister.layoutParams
            val paramsIn = btnIn.layoutParams

            if (isRegisterActive) {
                btnRegister.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B11719")))
                btnIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A7A8A9")))
                paramsIn.width = FrameLayout.LayoutParams.MATCH_PARENT
                btnIn.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                paramsRegister.width = dpToPx(150)
                btnRegister.z = 1f
                btnIn.z = 0f
                layoutRegister.visibility = View.VISIBLE
                layoutLogin.visibility = View.GONE
            } else {
                btnRegister.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A7A8A9")))
                btnIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B11719")))
                paramsRegister.width = FrameLayout.LayoutParams.MATCH_PARENT
                paramsIn.width = dpToPx(160)
                btnIn.z = 1f
                btnRegister.z = 0f
                layoutRegister.visibility = View.GONE
                layoutLogin.visibility = View.VISIBLE
            }
            btnRegister.layoutParams = paramsRegister
            btnIn.layoutParams = paramsIn
        }

        btnRegister.setOnClickListener {
            toggleButtons(true)
            // lógica de registro
        }

        btnIn.setOnClickListener {
            toggleButtons(false)
            // lógica de ingreso
        }

        val dbHelper = UserDBHelper(this)

        val name = findViewById<EditText>(R.id.txt_name)
        val surname = findViewById<EditText>(R.id.txt_surname)
        val txtEmail = findViewById<EditText>(R.id.txt_email)
        val txtPassword = findViewById<EditText>(R.id.txt_password)
        val repeatPassword = findViewById<EditText>(R.id.repeatPass)

        val btnRegisterMember = findViewById<Button>(R.id.btn_registerNewMember)

        btnRegisterMember.setOnClickListener {
            val nameString = name.text.toString().trim()
            val surnameString = surname.text.toString().trim()
            val txtEmailstring = txtEmail.text.toString().trim()
            val passwordString = txtPassword.text.toString().trim()
            val repeatPasswordString = repeatPassword.text.toString().trim()

            if (nameString.isEmpty() || surnameString.isEmpty() || txtEmailstring.isEmpty() || passwordString.isEmpty() || repeatPasswordString.isEmpty()) {
                Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (repeatPasswordString != passwordString) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val success = dbHelper.register(nameString, surnameString, txtEmailstring, passwordString)
                if (success) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "no se que sucede", Toast.LENGTH_SHORT).show()

                }
            }
        }

        val email = findViewById<EditText>(R.id.email)
        val pass = findViewById<EditText>(R.id.txt_pass)
        val btnLargeIn = findViewById<Button>(R.id.btn_large_in)

        btnLargeIn.setOnClickListener {
            val emailString = email.text.toString().trim()
            val passString = pass.text.toString().trim()

            if (emailString.isEmpty() || passString.isEmpty()) {
                Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(dbHelper.login(emailString, passString)){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
    }
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}
