package com.example.clubdeportivo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.app.DatePickerDialog
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.google.android.material.switchmaterial.SwitchMaterial

class NewMember : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)

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

            fun isValidEmail(email: String): Boolean {
                return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }

            fun isValidDate(dateStr: String): Boolean {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                format.isLenient = false

                return try {
                    val date = format.parse(dateStr)
                    date != null
                } catch (ex: Exception) {
                    false
                }
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
            if(!isValidDate(dateOfBirth)) {
                Toast.makeText(this, "Fecha inválida, use el formato dd/MM/yyyy", Toast.LENGTH_LONG).show()
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
                dateOfBirth = dateOfBirth,
                gender = gender,
                email = email,
                apto = switchApto.isChecked,
                pay = switchPay.isChecked
                )
                if (success) {
                    Toast.makeText(this, "Socio registrado correctamente", Toast.LENGTH_SHORT).show()
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
