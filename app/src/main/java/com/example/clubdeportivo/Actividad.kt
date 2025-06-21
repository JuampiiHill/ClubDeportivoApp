package com.example.clubdeportivo

data class Actividad(
    val id: Int,
    val nombre: String,
    val dia: String,
    val horario: String,
    val cupoDisponible: Int
)
