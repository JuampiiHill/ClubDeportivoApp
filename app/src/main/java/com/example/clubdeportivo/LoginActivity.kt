package com.example.clubdeportivo

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
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
                //alternar colores
                btnRegister.setBackgroundTintList(ColorStateList.valueOf("#B11719".toColorInt()))
                btnIn.setBackgroundTintList(ColorStateList.valueOf("#A7A8A9".toColorInt()))

                paramsIn.width = FrameLayout.LayoutParams.MATCH_PARENT
                paramsIn.height = dpToPx(58)
                btnIn.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                btnIn.gravity = Gravity.START or Gravity.CENTER_VERTICAL

                btnIn.layoutParams = paramsIn
                btnIn.requestLayout()
                btnIn.invalidate()

                btnRegister.textAlignment = View.TEXT_ALIGNMENT_CENTER
                paramsRegister.height = dpToPx(58)
                btnRegister.gravity = Gravity.CENTER
                paramsRegister.width = dpToPx(177)

                //hacer visible los diferentes formularios
                layoutRegister.visibility = View.VISIBLE
                layoutLogin.visibility = View.GONE

                //traer el btn_register al frente
                btnRegister.bringToFront()
                btnIn.z = 0f
            } else {
                //alternar colores
                btnRegister.setBackgroundTintList(ColorStateList.valueOf("#A7A8A9".toColorInt()))
                btnIn.setBackgroundTintList(ColorStateList.valueOf("#B11719".toColorInt()))

                paramsRegister.width = FrameLayout.LayoutParams.MATCH_PARENT
                paramsRegister.height = dpToPx(58)
                btnRegister.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                btnRegister.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                btnIn.textAlignment = View.TEXT_ALIGNMENT_CENTER
                btnIn.gravity = Gravity.CENTER
                paramsIn.width = dpToPx(177)

                //hacer visible los diferentes formularios
                layoutRegister.visibility = View.GONE
                layoutLogin.visibility = View.VISIBLE

                //traer btn_in al frente
                btnIn.z = 1f
                btnRegister.z = 0f
            }
            btnRegister.layoutParams = paramsRegister
            btnIn.layoutParams = paramsIn
        }

        btnRegister.setOnClickListener {
            toggleButtons(true)
        }

        btnIn.setOnClickListener {
            toggleButtons(false)
        }

        // Al hacer click en el "¿No tienes cuenta? Registrate" nos lleve al formulario de registro
        val startWithRegister = intent.getBooleanExtra("start_with_register", false)
        if (startWithRegister) {
           btnRegister.post {
              toggleButtons(true)
          }
        }


        val account = findViewById<TextView>(R.id.txt_account)
        val fullText = account.text.toString()
        val palabraClave = "Inicia sesión"
        val start = fullText.indexOf(palabraClave)

        if(start != -1){
            val end = start + palabraClave.length
            val spannable = SpannableString(fullText)
            spannable.setSpan(
                ForegroundColorSpan("#B11719".toColorInt()),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            account.text = spannable
        }

        account.setOnClickListener {
            toggleButtons(false)
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
            val txtEmailString = txtEmail.text.toString().trim()
            val passwordString = txtPassword.text.toString().trim()
            val repeatPasswordString = repeatPassword.text.toString().trim()

            if (nameString.isEmpty() || surnameString.isEmpty() || txtEmailString.isEmpty() || passwordString.isEmpty() || repeatPasswordString.isEmpty()) {
                Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (repeatPasswordString != passwordString) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val success = dbHelper.register(nameString, surnameString, txtEmailString, passwordString)
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
