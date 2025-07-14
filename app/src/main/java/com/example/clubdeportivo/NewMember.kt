package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.app.DatePickerDialog
import android.util.Log
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.google.android.material.switchmaterial.SwitchMaterial

class NewMember : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)

        // Configuración del Spinner
        val spinner = findViewById<Spinner>(R.id.spinnerGender)
        val options = arrayOf("Género","Femenino", "Masculino", "Otro")

        val adapter = object: ArrayAdapter<String>(this, R.layout.spinner_item, options) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(("#A7A8A9".toColorInt()))
                } else {
                    view.setTextColor(("#4A4A4A".toColorInt()))
                }
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Referencias a los campos del formulario
        val inputName = findViewById<EditText>(R.id.txt_name)
        val inputSurname = findViewById<EditText>(R.id.txt_surname)
        val inputDocument = findViewById<EditText>(R.id.txt_document)
        val inputDateOfBirth = findViewById<EditText>(R.id.txt_date)
        val inputEmail = findViewById<EditText>(R.id.txt_email)
        val switchApto = findViewById<SwitchMaterial>(R.id.switch_apto)
        val switchPay = findViewById<SwitchMaterial>(R.id.switch_pay)

        val paychecked = switchPay.isChecked
        Log.e("SWITCH DE PAGO", "dato: ${paychecked}")

        inputDateOfBirth.setOnClickListener {
            showDatePickerDialog(inputDateOfBirth)
        }

        val dbHelper = UserDBHelper(this)

        val btnRegister: Button = findViewById(R.id.btn_large_in)
        btnRegister.setOnClickListener {

            val name = inputName.text.toString()
            val surname = inputSurname.text.toString()
            val document = inputDocument.text.toString()
            val dateOfBirth = inputDateOfBirth.text.toString()
            val gender = spinner.selectedItem.toString()
            val email = inputEmail.text.toString()

            //Manejo de fechas
            val formatIn = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatOut = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val dateConvert: String = try {
                formatOut.format(formatIn.parse(dateOfBirth)!!)
            } catch (e: Exception) {
                Log.e("FECHA_CONVERT", "Error convirtiendo fecha: $dateOfBirth")
                return@setOnClickListener
            }

            fun isValidEmail(email: String): Boolean {
                return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

            if (name.isEmpty() || surname.isEmpty() || document.isEmpty() || dateOfBirth.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (gender == "Género") {
                Toast.makeText(this, "Debes seleccionar un género", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(!isValidEmail(email)) {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!switchApto.isChecked) {
                Toast.makeText(this, "Entregar el apto médico es obligatorio", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val success = dbHelper.addMember(
                name = name,
                surname = surname,
                document = document,
                dateOfBirth = dateConvert,
                gender = gender,
                email = email,
                apto = switchApto.isChecked,
                pay = switchPay.isChecked
                )
                if (success) {
                    startActivity(Intent(this, SuccessRegister::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar socio", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
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
        findViewById<LinearLayout>(R.id.nav_btn_members).setOnClickListener {
            startActivity(Intent(this, MembersList::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_activities).setOnClickListener {
            startActivity(Intent(this,
                ActivityList::class.java))
        }

        findViewById<LinearLayout>(R.id.nav_btn_payments).setOnClickListener {
            startActivity(Intent(this, PayFee::class.java))
        }
    }
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            // El mes empieza en 0, por eso sumamos 1
            val formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", selectedDayOfMonth, selectedMonth + 1, selectedYear)
            editText.setText(formattedDate)
        }, year, month, day)
        datePickerDialog.show()
    }
}
