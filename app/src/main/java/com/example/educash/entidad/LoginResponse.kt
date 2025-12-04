package com.example.educash.entidad

data class LoginResponse(
    val ok: Boolean,
    val message: String?,
    val token: String?,      // si aun no usas JWT puede llegar null
    val usuario: Usuario?
)