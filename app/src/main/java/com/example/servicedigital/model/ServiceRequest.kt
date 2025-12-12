package com.example.servicedigital.model

data class ServiceRequest(
    val titulo: String,
    val descripcion: String,
    val imagen: String,
    val contacto: String,
    val usuarioEmail: String?
)