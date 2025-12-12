package com.example.servicedigital


import InboxScreen
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.servicedigital.ui.theme.ServiceDigitalTheme
import com.example.servicedigital.view.ServiceCatalogScreen
import com.example.servicedigital.view.UploadServiceScreen
import com.example.servicedigital.view.LoginScreen
import com.example.servicedigital.view.RegisterScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.servicedigital.view.ChatScreen
import com.example.servicedigital.view.ChatViewModel
import com.example.servicedigital.view.QrErrorScreen
import com.example.servicedigital.view.QrScannerScreen
import com.example.servicedigital.view.ServiceViewModel
import com.example.servicedigital.view.UserQrScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceDigitalTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val chatViewModel: ChatViewModel = viewModel()
    val navController = rememberNavController()
    val serviceViewModel: ServiceViewModel = viewModel() //ViewModel compartido

    NavHost(
        navController = navController,
        startDestination = "login" // Pantalla inicial
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("inboxScreen/{nombreEmisor}") { backStackEntry ->
            val nombreEmisor = backStackEntry.arguments?.getString("nombreEmisor") ?: ""
            InboxScreen(navController, nombreEmisor, chatViewModel)
        }

        composable("catalogo/{nombreUsuario}") { backStackEntry ->
            val nombreUsuario = backStackEntry.arguments?.getString("nombreUsuario") ?: "Usuario"
            ServiceCatalogScreen(
                navController = navController,
                viewModel = serviceViewModel,
                userName = nombreUsuario,
                chatViewModel = chatViewModel
            )
        }
        composable("uploadService") {
            UploadServiceScreen(
                navController = navController,
                viewModel = serviceViewModel
            )
        }
        composable("chatScreen/{contacto}/{nombreEmisor}") { backStackEntry ->
            // 1. Decodificamos para que caracteres como '@' no rompan la app
            val contactoCodificado = backStackEntry.arguments?.getString("contacto") ?: ""
            val contacto = android.net.Uri.decode(contactoCodificado) // <--- ESTO ES CLAVE

            val nombreEmisor = backStackEntry.arguments?.getString("nombreEmisor") ?: ""

            // Pasamos los datos limpios a la pantalla
            ChatScreen(navController, contacto, nombreEmisor, chatViewModel)
        }

        composable("qrScanner/{userName}") { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "Usuario"
            QrScannerScreen(
                navController = navController,
                userName = userName
            )
        }
        
        // New route for QR errors
        composable("qrError/{userName}") { backStackEntry ->
             val userName = backStackEntry.arguments?.getString("userName") ?: "Usuario"
             QrErrorScreen(navController, userName)
        }

        // New route for User QR
        composable("userQr/{userName}") { backStackEntry ->
             val userName = backStackEntry.arguments?.getString("userName") ?: "Usuario"
             UserQrScreen(navController, userName)
        }

        // Removed duplicate route definition for chatScreen to avoid ambiguity, although NavHost usually handles first match.
        // But the second one had Uri.decode logic which might be important.
        // Merging logic into one composable block if needed, or keeping unique routes.
        // The previous code had two "chatScreen/{contacto}/{nombreEmisor}" blocks. 
        // I will keep the one with Uri.decode as it seems more robust for contacts passed as URIs.
        /* 
           The original code had:
           composable("chatScreen/{contacto}/{nombreEmisor}") { ... }
           composable("qrScanner/{userName}") { ... }
           composable("chatScreen/{contacto}/{nombreEmisor}") { ... Uri.decode ... }
           
           I will use the Uri.decode version as the single source of truth for this route.
        */
    }
}
