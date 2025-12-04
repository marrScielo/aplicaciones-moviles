package com.example.educash.service

import com.example.educash.entidad.CategoriaResponse
import com.example.educash.entidad.ModuloDetalleResponse
import com.example.educash.entidad.ModuloInfo

import com.example.educash.entidad.ModuloResponse
import com.example.educash.infomodulos.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriaService {

    @GET("/categorias")
    suspend fun getCategorias(): Response<CategoriaResponse>

    @GET("/categorias/{id}/modulos")
    suspend fun getModulos(@Path("id") categoriaId: Int): Response<ModuloResponse>

    @GET("/modulos/{id}/detalles")
    suspend fun getModuloDetalles(@Path("id") id: Int): Response<ModuloDetalleResponse>


    @GET("modulos/{moduloId}/info")
    suspend fun getModuloInfo(@Path("moduloId") moduloId: Int): Response<APIResponse>

}
val categoriaApi: CategoriaService by lazy {
    ApiClient.retrofit.create(CategoriaService::class.java)
}
