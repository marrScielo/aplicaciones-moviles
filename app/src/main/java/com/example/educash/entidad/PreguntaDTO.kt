package com.example.educash.entidad
import com.example.educash.entidad.OpcionDTO

data class PreguntaDTO(
    val id: Int,
    val enunciado: String,
    val nro: Int,
    val opciones: List<OpcionDTO> = emptyList()
)
