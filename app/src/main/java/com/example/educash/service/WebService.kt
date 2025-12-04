package com.example.educash.service

import com.example.educash.entidad.LoginRequest
import com.example.educash.entidad.LoginResponse
import com.example.educash.entidad.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.example.educash.service.authApi


object AppConstantes{
    const val URL_BASE = "http://192.168.18.89:3000"
}

interface WebService {

    @POST("/register")
    suspend fun register(@Body body: RegisterRequest): Response<Map<String, Any>>

    @POST("/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}

val authApi: WebService by lazy {
    ApiClient.retrofit.create(WebService::class.java)
}
