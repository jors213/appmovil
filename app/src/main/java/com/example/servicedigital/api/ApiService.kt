package com.example.servicedigital.api

import com.example.servicedigital.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.servicedigital.view.ServiceViewModel
import okhttp3.ResponseBody

data class UserRequest(
    val nombre: String? = null,
    val email: String,
    val password: String
)

data class ApiResponse(
    val message: String
)


interface ApiService {
    @POST("login")
    suspend fun login(@Body user: User): Response<ResponseBody>

    @POST("register")
    suspend fun register(@Body user: User): Response<ResponseBody>
}
