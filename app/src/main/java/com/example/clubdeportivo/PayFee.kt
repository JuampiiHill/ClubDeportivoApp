package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PayFee : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pay_fee)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val document = intent.getStringExtra("document") ?: ""
        val dbHelper = UserDBHelper(this)

        // Referencias a los campos del formulario
        val inputDocumento = findViewById<EditText>(R.id.txt_document)
        val inputNombre = findViewById<EditText>(R.id.txt_name)
        val inputApellido = findViewById<EditText>(R.id.txt_surname)
        val inputEmail = findViewById<EditText>(R.id.txt_email)
        val inputMount = findViewById<EditText>(R.id.txt_mount)
        val radioGroup = findViewById<RadioGroup>(R.id.payment_method_group)

        val member = dbHelper.getMemberByDocument(document)

        inputDocumento.text = member.document
        inputNombre.text = member.name
        inputApellido.text = member.surname
        inputEmail.text = member.email

        // Rellenar con datos del intent (desde el carnet)
        //inputDocumento.setText(intent.getStringExtra("documento") ?: "")
        //inputNombre.setText(intent.getStringExtra("nombre") ?: "")
        //inputApellido.setText(intent.getStringExtra("apellido") ?: "")
        //inputEmail.setText(intent.getStringExtra("email") ?: "")


        // Botón pagar
        val btnPay: Button = findViewById(R.id.btn_pay)
        btnPay.setOnClickListener {
           // val mountToString = inputMount.text.toString().trim()
            //val documento = inputDocumento.text.toString()
            //val metodoPago = when (radioGroup.checkedRadioButtonId) {
              //  R.id.rb_efectivo -> "Efectivo"
                //R.id.rb_tarjeta -> "Tarjeta"
                //else -> null
            }

            //if (metodoPago == null) {
              //  Toast.makeText(this, "Seleccioná un método de pago", Toast.LENGTH_SHORT).show()
                //return@setOnClickListener
            //}

            //if (mountToString.isEmpty()) {
              //  Toast.makeText(this, "Ingrese un monto", Toast.LENGTH_SHORT).show()
            //} else {
              //  val nuevoVencimiento = LocalDate.now().plusDays(30).toString()
                //val exito = dbHelper.actualizarPago(documento, nuevoVencimiento, metodoPago)

                //if (exito) {
                  //  Toast.makeText(this, "Pago registrado", Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(this, SuccessPay::class.java))
                    //finish()
                //} else {
                  //  Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
                //}
            //}
        //}

        // Botón volver
        findViewById<Button>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Barra de navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.nav_btn_members).setOnClickListener {
            startActivity(Intent(this, MembersList::class.java))
        }
        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            startActivity(Intent(this, ActivityList::class.java))
        }
        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            startActivity(Intent(this, PayFee::class.java))
        }
    }
}
