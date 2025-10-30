package com.example.servicedigital.view

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicedigital.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.*

class ChatViewModel : ViewModel() {

    // Mapa de mensajes: cada contacto tiene su propia lista
    private val _chatMessages = mutableStateMapOf<String, SnapshotStateList<Message>>()
    val chatMessages: Map<String, SnapshotStateList<Message>> = _chatMessages

    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun getMessagesFor(contacto: String): SnapshotStateList<Message> {
        return _chatMessages.getOrPut(contacto) {
            mutableStateListOf(
                Message(1, "them", "Hola, ¿cómo estás?", sdf.format(Date())),
                Message(2, "me", "Bien, gracias. ¿Y tú?", sdf.format(Date()))
            )
        }
    }

    fun sendMessage(contacto: String, text: String) {
        val list = _chatMessages.getOrPut(contacto) { mutableStateListOf() }

        viewModelScope.launch {
            delay(400L) // Simula tiempo de envío (0.4 segundos)

            val newMessage = Message(
                id = list.size + 1,
                sender = "me",
                text = text,
                time = sdf.format(Date())
            )
            list.add(newMessage)

            delay(1200L) // Simula la respuesta del otro usuario
            val reply = Message(
                id = list.size + 1,
                sender = "them",
                text = "Recibido 👍",
                time = sdf.format(Date())
            )
            list.add(reply)
        }
    }

}