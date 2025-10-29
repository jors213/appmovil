package com.example.servicedigital.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.util.*

@Composable
fun ChatScreen(navController: NavController, contacto: String) {
    var input by remember { mutableStateOf(TextFieldValue("")) }

    // Mensajes simulados iniciales
    var messages by remember {
        mutableStateOf(
            mutableListOf(
                Message(1, "them", "Hola, estoy disponible mañana a las 10am.", "10:12"),
                Message(2, "me", "Perfecto, me sirve esa hora.", "10:15")
            )
        )
    }

    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFEFF7FB)) {
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {

            // Título con botón atrás
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = com.example.servicedigital.R.drawable.icons8arrow24),
                        contentDescription = "Volver",
                    )
                }

                Text(
                    text = contacto,
                    color = Color(0xFF0D47A1),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(40.dp)) // para balancear el icono
            }

            Spacer(modifier = Modifier.height(8.dp))

            //  Lista de mensajes
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

            Spacer(modifier = Modifier.height(4.dp))

            // Caja de texto + botón enviar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (input.text.isNotBlank()) {
                            val newMessage = Message(
                                id = messages.size + 1,
                                sender = "me",
                                text = input.text,
                                time = sdf.format(Date())
                            )
                            messages = (messages + newMessage).toMutableList()
                            input = TextFieldValue("")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Text("Enviar", color = Color.White)
                }
            }
        }
    }
}