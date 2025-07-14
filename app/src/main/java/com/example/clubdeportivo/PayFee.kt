package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

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

        val userName = intent.getStringExtra("nombre_usuario")


        val document = intent.getStringExtra("document") ?: ""
        val name = intent.getStringExtra("name") ?: ""
        val surname = intent.getStringExtra("surname") ?: ""
        val email = intent.getStringExtra("email") ?: ""

        // Referencias a los campos del formulario
        val txtDocument = findViewById<EditText>(R.id.txt_document)
        val txtName = findViewById<EditText>(R.id.txt_name)
        val txtSurname = findViewById<EditText>(R.id.txt_surname)
        val txtEmail = findViewById<EditText>(R.id.txt_email)
        val inputMount = findViewById<EditText>(R.id.txt_mount)
        val radioGroup = findViewById<RadioGroup>(R.id.payment_method_group)

        val btnPay: Button = findViewById(R.id.btn_pay)

        txtDocument.setText(document)
        txtName.setText(name)
        txtSurname.setText(surname)
        txtEmail.setText(email)

        val dbHelper = UserDBHelper(this)

        //Si no se reciben los datos por CarnetActivity al ingresar el documento del socio los demás campos serán completados automáticamente
        txtDocument.setOnFocusChangeListener{_, hasFocus ->
            if (!hasFocus) {
                val doc = txtDocument.text.toString()
                if (doc.isNotBlank()) {
                    val member = dbHelper.getMemberByDocument(doc)
                    if (member != null) {
                        txtName.setText(member.name)
                        txtSurname.setText(member.surname)
                        txtEmail.setText(member.email)

                        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        val today = java.util.Calendar.getInstance().time
                        val expirationDate = try {
                            formatter.parse(member.expirationDate)
                        } catch (e: Exception) {
                            null
                        }

                        if (expirationDate != null && expirationDate.after(today)) {
                            val formattedExpiration = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(expirationDate)
                            val dialog = android.app.AlertDialog.Builder(this)
                                .setTitle("Cuota al día")
                                .setMessage("La cuota está paga hasta el $formattedExpiration. ¿Desea pagar de todas formas?")
                                .setPositiveButton("SI") {_, _ ->
                                    btnPay.isEnabled = true
                                }
                                .setNegativeButton("NO") {_, _ ->
                                    btnPay.isEnabled = false
                                }
                                .create()
                            dialog.show()
                        } else {
                            btnPay.isEnabled = true
                        }
                    } else {
                        Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                        txtName.setText("")
                        txtSurname.setText("")
                        txtEmail.setText("")
                    }
                }
            }
        }

        // Botón pagar
        btnPay.setOnClickListener {
            val mountToString = inputMount.text.toString().trim()
            val document = txtDocument.text.toString()
            val paymentMethod = when (radioGroup.checkedRadioButtonId) {
                R.id.rb_efectivo -> "Efectivo"
                R.id.rb_tarjeta -> "Tarjeta"
                else -> null
            }

            if (paymentMethod == null) {
                Toast.makeText(this, "Seleccioná un método de pago", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mountToString.isEmpty()) {
                Toast.makeText(this, "Ingrese un monto", Toast.LENGTH_SHORT).show()
            } else {
                val nuevoVencimiento = LocalDate.now().plusMonths(1).toString()
                val success = dbHelper.updatePayment(document, nuevoVencimiento, paymentMethod)

                if (success) {
                    Toast.makeText(this, "Pago registrado", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SuccessPay::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar el pago", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón volver
        findViewById<Button>(R.id.btn_back).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }

        // Barra de navegación inferior
        findViewById<LinearLayout>(R.id.nav_btn_home).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.nav_btn_members).setOnClickListener {
            val intent = Intent(this, MembersList::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            val intent = Intent(this, ActivityList::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            val intent = Intent(this, PayFee::class.java)
            intent.putExtra("nombre_usuario", userName)
            startActivity(intent)
        }
    }
}
