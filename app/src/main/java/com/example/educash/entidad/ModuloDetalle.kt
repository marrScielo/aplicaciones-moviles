package com.example.educash.entidad

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuloDetalle(
    val id: Int,
    val titulo: String,
    val contenido: String,
    val imagen: String,
    val orden: Int
) : Parcelable




