package com.example.educash.service

import com.example.educash.entidad.QuizResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApi {
    @GET("/quiz")
    suspend fun getQuiz(@Query("temaId") temaId: Int): Response<QuizResponse>
}
