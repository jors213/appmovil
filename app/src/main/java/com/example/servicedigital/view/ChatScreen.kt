package com.example.servicedigital.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.servicedigital.R

@Composable
fun ChatScreen(
    navController: NavController,
    contacto: String,
    nombreEmisor: String,
    chatViewModel: ChatViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        chatViewModel.conectarAlChat(nombreEmisor)
    }
    val messages = chatViewModel.getMessagesFor(contacto)
    var input by remember { mutableStateOf(TextFieldValue("")) }

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(12.dp)
            .statusBarsPadding()

    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8arrow24),
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = contacto,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Divider(color = MaterialTheme.colorScheme.outlineVariant)

        //LISTA DE MENSAJES
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->

                val isMe = msg.sender == "me" || msg.sender == nombreEmisor

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {

                    // BURBUJA
                    Surface(
                        color = if (isMe) primaryColor else surfaceColor,
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 2.dp
                    ) {
                        Text(
                            text = msg.text,
                            modifier = Modifier.padding(10.dp),
                            color = if (isMe) onPrimaryColor else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // HORA DEBAJO
                    Text(
                        text = msg.time,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp, end = if (isMe) 4.dp else 0.dp)
                    )
                }
            }
        }

        // INPUT Y BOTÓN ENVIAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // BasicTextField doesn't support colors param directly for background, wrapping in Surface or Box
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = input,
                    onValueChange = { input = it },
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (input.text.isNotBlank()) {
                        chatViewModel.sendMessage(contacto, input.text, nombreEmisor)
                        input = TextFieldValue("")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Enviar", color = onPrimaryColor)
            }
        }
    }
}
