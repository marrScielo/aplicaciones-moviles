package com.example.educash.entidad

data class QuizResponse(
    val ok: Boolean,
    val data: List<PreguntaDTO> = emptyList()
)
