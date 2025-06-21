package com.example.clubdeportivo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewActivityFormActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_form)

        dbHelper = UserDBHelper(this)

        val editNombre = findViewById<EditText>(R.id.editNombre)
        val editDia = findViewById<EditText>(R.id.editDia)
        val editHorario = findViewById<EditText>(R.id.editHorario)
        val editCupo = findViewById<EditText>(R.id.editCupo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        btnGuardar.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val dia = editDia.text.toString().trim()
            val horario = editHorario.text.toString().trim()
            val cupoStr = editCupo.text.toString().trim()

            if (nombre.isEmpty() || dia.isEmpty() || horario.isEmpty() || cupoStr.isEmpty()) {
                Toast.makeText(this, "Por favor completá todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cupo = cupoStr.toIntOrNull()
            if (cupo == null || cupo <= 0) {
                Toast.makeText(this, "Cupo debe ser un número mayor a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val insertado = dbHelper.insertarActividad(nombre, dia, horario, cupo)
            if (insertado) {
                Toast.makeText(this, "Actividad guardada con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar la actividad", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
