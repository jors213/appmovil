package com.example.servicedigital.controller

import com.example.servicedigital.api.ApiService
import com.example.servicedigital.model.User
import com.example.servicedigital.network.RetrofitClient

class UserController {
    private val api = RetrofitClient.instance.create(ApiService::class.java)

    suspend fun login(email: String, password: String): String {
        val response = api.loginUser(User(nombre = "", email = email, password = password))
        return response.body()?.get("message") ?: "Error al conectar con el servidor"
    }

    suspend fun register(nombre: String, email: String, password: String): String {
        val response = api.registerUser(User(nombre, email, password))
        return response.body()?.get("message") ?: "Error al conectar con el servidor"
    }
}
