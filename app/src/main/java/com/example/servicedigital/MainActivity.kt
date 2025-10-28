package com.example.servicedigital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.servicedigital.ui.theme.ServiceDigitalTheme
import com.example.servicedigital.view.LoginScreen
import com.example.servicedigital.view.RegisterScreen
import com.example.servicedigital.view.ServiceCatalogScreen
import com.example.servicedigital.view.UploadServiceScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            Surface(color = MaterialTheme.colorScheme.background){
                NavHost(navController = navController, startDestination = "login"){
                    composable("login") { LoginScreen(navController) }
                    composable("register") { RegisterScreen(navController) }
                    composable("catalog"){ ServiceCatalogScreen(navController) }
                    composable("uploadService") { UploadServiceScreen(navController) }
                }
            }
            ServiceDigitalTheme {

            }
        }
    }
}