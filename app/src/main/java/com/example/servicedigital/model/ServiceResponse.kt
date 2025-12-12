package com.example.servicedigital.model

data class ServiceResponse(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val contacto: String,
    val usuarioEmail: String?
)