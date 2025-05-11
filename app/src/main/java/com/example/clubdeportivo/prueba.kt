/*package com.example.clubdeportivo

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class prueba : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.prueba)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnIn: Button = findViewById(btn_in)
        val btnRegister: Button = findViewById(R.id.btn_register)
        val layoutRegister: LayoutLinear = findViewById(R.id.register_container)
        val layoutLogin: LayoutLinear = findViewById(R.id.login_container)

        fun toggleButtons(isRegisterActive: Boolean) {
            val paramsRegister = btnRegister.layoutParams
            val paramsIn = btnIn.layoutParams


            if (isRegisterActive) {
                btnRegister.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B11719")))
                btnIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A7A8A9")))
                paramsIn.width = FrameLayout.LayoutParams.MATCH_PARENT
                btnIn.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                paramsRegister.width = dpToPx2(150)
                btnRegister.z = 1f
                btnIn.z = 0f
                layoutRegister.visibility = View.VISIBLE
                layoutLogin.visibility = View.GONE
            } else {
                btnRegister.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#A7A8A9")))
                btnIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B11719")))
                paramsRegister.width = FrameLayout.LayoutParams.MATCH_PARENT
                paramsIn.width = dpToPx2(140)
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
    }
}
fun dpToPx2(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}*/