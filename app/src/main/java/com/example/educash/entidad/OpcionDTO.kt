package com.example.educash.entidad

import com.google.gson.annotations.SerializedName

data class OpcionDTO(
    val id: Int,
    val texto: String,
    @SerializedName("es_correcta")
    val es_correcta: Boolean? = false
)
