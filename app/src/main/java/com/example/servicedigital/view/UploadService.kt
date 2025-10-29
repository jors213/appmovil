package com.example.servicedigital.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.servicedigital.R
import com.example.servicedigital.view.ServiceViewModel

@Composable
fun UploadServiceScreen(
    navController: NavController,
    viewModel: ServiceViewModel = viewModel()
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val buttonColor = Color(0xFF64B5F6)
    val textColor = Color(0xFF0D47A1)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFE3F2FD)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Subir Servicio",
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del servicio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Correo o número de contacto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (titulo.isBlank() || descripcion.isBlank() || contacto.isBlank()) {
                        mensaje = "Por favor, completa todos los campos"
                    } else {
                        mensaje = "Servicio publicado con éxito ✅"
                        val nuevoServicio = Servicio(
                            id = viewModel.servicios.size + 1,
                            titulo = titulo,
                            descripcion = descripcion,
                            imagen = R.drawable.placeholder,
                            contacto = contacto
                        )
                        viewModel.agregarServicio(nuevoServicio)

                        // Vuelve al catálogo
                        navController.navigate("catalog") {
                            popUpTo("catalog") { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar servicio", color = Color.White)
            }

            if (mensaje.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(mensaje, color = if (mensaje.contains("éxito")) Color(0xFF2E7D32) else Color.Red)
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Cancelar", color = textColor)
            }
        }
    }
}