package com.example.servicedigital.network

import com.example.servicedigital.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.servicedigital.view.ServiceViewModel

object RetrofitClient {
    // Use 10.0.2.2 for Android Emulator to access localhost of the host machine
    private const val BASE_URL = "http://10.0.2.2:8080/" 

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}