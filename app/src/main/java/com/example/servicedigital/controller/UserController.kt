package com.example.servicedigital.controller

import com.example.servicedigital.api.ApiService
import com.example.servicedigital.model.User
import com.example.servicedigital.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UserController {
    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        try {
            // Se envía el usuario al backend
            val response = api.login(User(email = email, password = password, nombre = ""))

            if (response.isSuccessful) {
                val json = JSONObject(response.body()?.string() ?: "")
                return@withContext User(
                    nombre = json.optString("nombre", ""),
                    email = json.optString("email", ""),
                    password = ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(nombre: String, email: String, password: String): String = withContext(Dispatchers.IO) {
        try {
            val response = api.register(User(nombre = nombre, email = email, password = password))

            if (response.isSuccessful) {
                "Registro exitoso"
            } else {
                "Error: ${response.code()}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "error"
        }
    }
}
