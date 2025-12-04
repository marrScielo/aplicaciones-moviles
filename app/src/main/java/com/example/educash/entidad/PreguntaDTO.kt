package com.example.educash.entidad

data class PreguntaDTO(
    val id: Int,
    val enunciado: String,
    val nro: Int,
    val opciones: List<OpcionDTO> = emptyList()
)
