package com.example.servicedigital.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.servicedigital.model.Message
import java.text.SimpleDateFormat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.servicedigital.R
import com.example.servicedigital.view.ChatViewModel
import java.util.*

@Composable
fun ChatScreen(navController: NavController, contacto: String, chatViewModel: ChatViewModel = viewModel()) {
    //val messages = remember { chatViewModel.getMessagesFor(contacto) }
    val messages = chatViewModel.getMessagesFor(contacto)
    var input by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Botón para volver
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.icons8arrow24),
                contentDescription = "Volver",
                tint = Color(0xFF0D47A1),
                modifier = Modifier.size(28.dp)
            )
        }

        // Lista de mensajes
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                val alignment = if (msg.sender == "me") Arrangement.End else Arrangement.Start
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = alignment
                ) {
                    Surface(
                        color = if (msg.sender == "me") Color(0xFF64B5F6) else Color.White,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = msg.text,
                            modifier = Modifier.padding(10.dp),
                            color = if (msg.sender == "me") Color.White else Color.Black
                        )
                    }
                }
            }
        }

        // Campo de texto + botón enviar
        Row(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Button(onClick = {
                if (input.text.isNotBlank()) {
                    chatViewModel.sendMessage(contacto, input.text)
                    input = TextFieldValue("")
                }
            }) {
                Text("Enviar")
            }
        }
    }
}