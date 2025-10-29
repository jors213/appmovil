package com.example.servicedigital.model

// Message/Conversation modelos
data class Message(
    val id: Int,
    val sender: String,// "me" o "them" (o email/nombre)
    val text: String,
    val time: String
)

data class Conversation(
    val id: Int,
    val contactName: String,
    val avatarRes: Int,        // drawable resource id para avatar
    val messages: MutableList<Message>
)
