package com.example.educash.entidad

data class RegisterRequest(
    val email: String,
    val password: String,
    val nombreyapellido: String
)
