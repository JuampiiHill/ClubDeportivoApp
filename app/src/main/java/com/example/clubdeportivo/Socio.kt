package com.example.clubdeportivo

data class Member(
    val id: Int,
    val name: String,
    val surname: String,
    val document: String,
    val dateOfBirth: String,
    val gender: String,
    val email: String,
    val apto: Boolean,
    val pay: Boolean,
    val expirationDate: String,
    val paymentMethod: String? = null
)
