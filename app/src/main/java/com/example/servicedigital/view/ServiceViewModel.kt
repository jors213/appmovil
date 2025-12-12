package com.example.servicedigital.view

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicedigital.model.ServiceRequest
import com.example.servicedigital.model.ServiceResponse
import com.example.servicedigital.network.RetrofitClient
import com.example.servicedigital.view.ServiceViewModel
import kotlinx.coroutines.launch

class ServiceViewModel : ViewModel() {
    var servicios = mutableStateListOf<ServiceResponse>()
        private set
    init {
        cargarServicios()
    }
    //private val _servicios = mutableStateListOf<Servicio>()
    //val servicios: List<Servicio> get() = _servicios

    fun cargarServicios() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getServices()
                if (response.isSuccessful){
                    servicios.clear()
                    servicios.addAll(response.body()?: emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun subirServicio(service: ServiceRequest): Boolean{
        val response = RetrofitClient.instance.createService(service)
        return response.isSuccessful
    }
}