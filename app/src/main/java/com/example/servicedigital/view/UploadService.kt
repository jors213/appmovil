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
import com.example.servicedigital.api.ApiService
import com.example.servicedigital.model.ServiceRequest
import com.example.servicedigital.network.RetrofitClient
import com.example.servicedigital.view.ServiceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UploadServiceScreen(
    navController: NavController,
    viewModel: ServiceViewModel = viewModel(),
    userName: String = "Usuario"
) {
    var isUploading by remember { mutableStateOf(false) }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var contacto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Subir Servicio",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del servicio") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contacto,
                onValueChange = { contacto = it },
                label = { Text("Correo o número de contacto") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (titulo.isBlank() || descripcion.isBlank() || contacto.isBlank()) {
                        mensaje = "Por favor, completa todos los campos"
                    } else if (!isUploading) {
                        isUploading = true
                        mensaje = "Subiendo servicio..."

                        coroutineScope.launch {
                            delay(800) // animación opcional

                            val request = ServiceRequest(
                                titulo = titulo,
                                descripcion = descripcion,
                                imagen = "placeholder.png",
                                contacto = contacto,
                                usuarioEmail = userName
                            )

                            try {
                                // Use ViewModel method if available or direct Retrofit call, but be careful with threads
                                // Here we are in coroutineScope.launch which is Main thread by default for Compose scope? 
                                // Retrofit suspend functions are main-safe.
                                
                                // Better to use ViewModel to hold logic and survive config changes, but direct call is ok for simple prototype.
                                // However, the user previously injected ServiceViewModel.
                                // Let's try to use the ViewModel method if it exists or create one.
                                // The ServiceViewModel provided has a 'subirServicio' method.
                                
                                val success = viewModel.subirServicio(request)

                                isUploading = false

                                if (success) {
                                    mensaje = "Servicio publicado con éxito"
                                    
                                    // Reload services in ViewModel to reflect changes in Catalog
                                    viewModel.cargarServicios()

                                    //volver al catálogo
                                    navController.navigate("catalogo/$userName") {
                                        popUpTo("catalogo/$userName") { inclusive = true }
                                    }

                                } else {
                                    mensaje = "Error al publicar el servicio (Server Error)"
                                }
                            } catch (e: Exception) {
                                isUploading = false
                                mensaje = "Error de conexión: ${e.localizedMessage}"
                                e.printStackTrace()
                            }
                        }
                    }
                },
                enabled = !isUploading,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    if (isUploading) "Subiendo..." else "Publicar servicio",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (mensaje.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    mensaje,
                    color = if (mensaje.contains("éxito")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Cancelar", color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}
