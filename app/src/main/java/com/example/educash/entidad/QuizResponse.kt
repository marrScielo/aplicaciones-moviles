package com.example.educash.entidad

import com.example.educash.entidad.PreguntaDTO
data class QuizResponse(
    val ok: Boolean,
    val data: List<PreguntaDTO> = emptyList()
)
