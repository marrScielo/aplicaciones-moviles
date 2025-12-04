package com.example.educash.servic
import com.example.educash.entidad.PreguntaDTO
import com.example.educash.entidad.QuizResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface QuizApi {

    @GET("quiz/{moduloId}")
    suspend fun getQuiz(
        @Path("moduloId") id: Int
    ): Response<QuizResponse>
}
