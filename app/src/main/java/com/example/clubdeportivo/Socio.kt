package com.example.clubdeportivo

data class Socio(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val documento: String,
    val nacimiento: String,
    val genero: String,
    val email: String,
    val apto: Boolean,
    val pago: Boolean,
    val vencimiento: String,
    val metodoPago: String? = null
)
