package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AssignActivityActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper
    private lateinit var inputDni: EditText
    private lateinit var btnAsignar: Button
    private lateinit var txtActividad: TextView
    private lateinit var nombreActividad: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_activity)

        dbHelper = UserDBHelper(this)

        inputDni = findViewById(R.id.input_dni)
        btnAsignar = findViewById(R.id.btn_assign)
        txtActividad = findViewById(R.id.txt_nombre_actividad)

        // Obtener nombre de actividad desde el intent
        nombreActividad = intent.getStringExtra("nombreActividad") ?: "Actividad desconocida"
        txtActividad.text = nombreActividad

        btnAsignar.setOnClickListener {
            val dni = inputDni.text.toString().trim()

            if (dni.isEmpty()) {
                Toast.makeText(this, "Ingrese un DNI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val member = dbHelper.getMemberByDocument(dni)
            if (member == null) {
                Toast.makeText(this, "Socio no encontrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val activity = dbHelper.getActivityByName(nombreActividad)
            if (activity == null) {
                Toast.makeText(this, "Actividad no encontrada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (activity.availableSpace <= 0) {
                Toast.makeText(this, "Sin cupo disponible", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = dbHelper.assignActivity(member.id, activity.id, member.document, activity.name)

            if (exito) {
                Toast.makeText(this, "Actividad asignada con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al asignar actividad", Toast.LENGTH_SHORT).show()
            }
        }

        val btnBack: Button = findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

}