package com.example.servicedigital.view

import CameraPreview
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    navController: NavController,
    userName: String
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    // Pedimos el permiso la primera vez que se abre la pantalla
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear QR") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Cerrar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (hasCameraPermission) {
                // Aquí ya tenemos permiso → mostramos cámara
                CameraPreview(
                    onQrScanned = { value ->
                        try {
                            // Intenta procesar el QR
                            if (value.isBlank()) {
                                throw Exception("Código vacío")
                            }
                            
                            // Validar formato si es necesario (opcional)
                            // if (!value.contains("@")) throw Exception("Formato de contacto inválido")

                            val encoded = Uri.encode(value)
                            navController.navigate("chatScreen/$encoded/$userName")
                            
                        } catch (e: Exception) {
                            // Si algo falla (código incompatible), vamos a la pantalla de error
                            navController.navigate("qrError/$userName")
                        }
                    }
                )
            } else {
                // Sin permiso → mostramos mensaje y botón
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Se necesita permiso de cámara para escanear QR")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        launcher.launch(Manifest.permission.CAMERA)
                    }) {
                        Text("Dar permiso")
                    }
                }
            }
        }
    }
}
