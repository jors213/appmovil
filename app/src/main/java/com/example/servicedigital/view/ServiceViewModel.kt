package com.example.servicedigital.view

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.servicedigital.view.ServiceViewModel

class ServiceViewModel : ViewModel() {
    private val _servicios = mutableStateListOf<Servicio>()
    val servicios: List<Servicio> get() = _servicios

    fun agregarServicio(servicio: Servicio) {
        _servicios.add(servicio)
    }
}