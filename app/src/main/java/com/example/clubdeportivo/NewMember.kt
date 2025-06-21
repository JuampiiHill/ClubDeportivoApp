package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class NewMember : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)

        val dbHelper = UserDBHelper(this)

        // Referencias a los campos del formulario
        val inputNombre = findViewById<EditText>(R.id.txt_name)
        val inputApellido = findViewById<EditText>(R.id.txt_surname)
        val inputDocumento = findViewById<EditText>(R.id.txt_document)
        val inputNacimiento = findViewById<EditText>(R.id.txt_date)
        val inputGenero = findViewById<EditText>(R.id.txt_gender)
        val inputEmail = findViewById<EditText>(R.id.txt_email)
        val switchApto = findViewById<Switch>(R.id.switch_apto)
        val switchPago = findViewById<Switch>(R.id.switch_pago)

        val btnRegister: Button = findViewById(R.id.btn_large_in)
        btnRegister.setOnClickListener {
            val exito = dbHelper.insertarSocio(
                nombre = inputNombre.text.toString(),
                apellido = inputApellido.text.toString(),
                documento = inputDocumento.text.toString(),
                nacimiento = inputNacimiento.text.toString(),
                genero = inputGenero.text.toString(),
                email = inputEmail.text.toString(),
                apto = switchApto.isChecked,
                pago = switchPago.isChecked
            )

            if (exito) {
                Toast.makeText(this, "Socio registrado correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SuccessRegister::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_LONG).show()
            }
        }

        val btnBack: Button = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Eventos de navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            startActivity(Intent(this,
                ActivityList::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            startActivity(Intent(this, PayFee::class.java))
        }
    }
}
