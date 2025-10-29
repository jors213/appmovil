package com.example.servicedigital


import InboxScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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

import com.example.servicedigital.view.ServiceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServiceDigitalTheme {
                Surface(color = Color.White) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val serviceViewModel: ServiceViewModel = viewModel() // 👈 ViewModel compartido

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
        composable("inboxScreen"){
            InboxScreen(navController)
        }

        composable("catalog/{nombreUsuario}") { backStackEntry ->
            val nombreUsuario = backStackEntry.arguments?.getString("nombreUsuario") ?: "Usuario"
            ServiceCatalogScreen(
                navController = navController,
                viewModel = serviceViewModel,
                userName = nombreUsuario
            )
        }
        composable("uploadService") {
            UploadServiceScreen(
                navController = navController,
                viewModel = serviceViewModel
            )
        }
        composable ( "chatScreen/{contacto}" ) {backStackEntry ->
            val contacto = backStackEntry.arguments?.getString("contacto") ?: ""
            ChatScreen(navController, contacto)
        }
        composable ("chatScreen"){
            ChatScreen(navController, contacto = "general")
        }
    }
}