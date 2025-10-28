package com.example.servicedigital.api

import com.example.servicedigital.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("register")
    suspend fun registerUser(@Body user: User): Response<Map<String, String>>

    @POST("login")
    suspend fun loginUser(@Body user: User): Response<Map<String, String>>
}