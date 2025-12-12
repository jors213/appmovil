package com.example.servicedigital.view

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicedigital.model.Conversation
import com.example.servicedigital.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel : ViewModel() {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    // Mapa de mensajes: cada contacto tiene su propia lista
    private val _chatMessages = mutableStateMapOf<String, SnapshotStateList<Message>>()
    val chatMessages: Map<String, SnapshotStateList<Message>> = _chatMessages

    // Lista de conversaciones activas
    private val _conversations = mutableStateListOf<Conversation>()
    val conversations: SnapshotStateList<Conversation> = _conversations

    private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    // FUNCIÓN PARA CONECTARSE (LLAMAR DESDE LA VISTA) ---
    fun conectarAlChat(usuario: String) {
        // Evitar reconexiones si ya existe
        if (webSocket != null) return

        val request = Request.Builder()
            // IMPORTANTE: Si usas emulador: "ws://10.0.2.2:8080/chat"
            // Si usas celular físico: "ws://TU_IP_PC:8080/chat"
            .url("ws://10.0.2.2:8080/chat")
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ChatViewModel", "Conectado al servidor WebSocket")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("ChatViewModel", "Mensaje recibido del servidor: $text")
                // Aquí podrías procesar la respuesta si quisieras mostrarla en la UI
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("ChatViewModel", "Cerrando conexión: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("ChatViewModel", "Error de conexión: ${t.message}")
            }
        }

        webSocket = client.newWebSocket(request, listener)
    }

    fun getMessagesFor(contacto: String): SnapshotStateList<Message> {
        return _chatMessages.getOrPut(contacto) {
            mutableStateListOf()
        }
    }

    //FUNCIÓN DE ENVIAR (AHORA SÍ ENVÍA AL SERVIDOR)
    fun sendMessage(contacto: String, text: String, sender: String) {
        val list = _chatMessages.getOrPut(contacto) { mutableStateListOf() }
        val currentTime = sdf.format(Date())

        //Agregar mensaje a la UI localmente para que el usuario lo vea
        val newMessage = Message(
            id = list.size + 1,
            sender = "me", // "me" para que salga a la derecha
            text = text,
            time = currentTime
        )
        list.add(newMessage)
        updateConversation(contacto, text, currentTime)

        //ENVIAR AL SERVIDOR (BACKEND)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Creamos un JSON simple para enviar
                val json = JSONObject()
                json.put("sender", sender)
                json.put("receiver", contacto)
                json.put("message", text)

                // Enviamos por el socket
                val enviado = webSocket?.send(json.toString())

                if (enviado == true) {
                    Log.d("ChatViewModel", "Mensaje enviado al servidor: $text")
                } else {
                    Log.e("ChatViewModel", "No se pudo enviar (Socket desconectado)")
                    // Intento de reconexión básico
                    conectarAlChat(sender)
                }
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error enviando: ${e.message}")
            }
        }
    }

    private fun updateConversation(contactName: String, lastMessage: String, time: String) {
        val existingConv = _conversations.find { it.contactName == contactName }
        if (existingConv != null) {
            existingConv.messages.add(Message(existingConv.messages.size + 1, "me", lastMessage, time))
        } else {
            val newConv = Conversation(
                id = _conversations.size + 1,
                contactName = contactName,
                avatarRes = com.example.servicedigital.R.drawable.fotousuario,
                messages = mutableListOf(Message(1, "me", lastMessage, time))
            )
            _conversations.add(0, newConv)
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "ViewModel cleared")
    }
}
