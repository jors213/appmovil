package com.example.servicedigital.api

import com.example.servicedigital.model.ServiceRequest
import com.example.servicedigital.model.ServiceResponse
import com.example.servicedigital.model.User

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.servicedigital.view.ServiceViewModel
import okhttp3.ResponseBody
import retrofit2.http.GET

interface ApiService {

    @POST("login")
    suspend fun login(@Body user: User): Response<ResponseBody>

    @POST("register")
    suspend fun register(@Body user: User): Response<ResponseBody>

    @POST("services")
    suspend fun createService(@Body service: ServiceRequest): Response<Unit>

    @GET("services")
    suspend fun getServices(): Response<List<ServiceResponse>>
}
