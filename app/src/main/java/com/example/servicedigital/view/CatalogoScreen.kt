package com.example.servicedigital.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.servicedigital.R

data class Servicio(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: Int
)

@Composable
fun ServiceCatalogScreen(navController: NavController,
    userName: String = "Usuario"
) {
    val backgroundColor = Color(0xFFE3F2FD)
    val buttonColor = Color(0xFF64B5F6)
    val textColor = Color(0xFF0D47A1)

    var showSidePanel by remember { mutableStateOf(false) }

    val panelOffset by animateDpAsState(
        targetValue = if (showSidePanel) 0.dp else 250.dp,
        label = "panelOffset"
    )

    val servicios = listOf(
        Servicio(1, "Instalación de gas certificada", "Técnico autorizado SEC", R.drawable.placeholder),
        Servicio(2, "Certificación eléctrica", "Ingeniero en electricidad", R.drawable.placeholder),
        Servicio(3, "Mantención de calefón", "Servicio a domicilio", R.drawable.placeholder),
        Servicio(4, "Revisión de red de gas", "Profesional con experiencia", R.drawable.placeholder),
        Servicio(5, "Certificado de instalación", "Entrega en 24 horas", R.drawable.placeholder),
        Servicio(6, "Gasfitería general", "Reparaciones y certificaciones", R.drawable.placeholder)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("uploadService") },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Subir servicio", color = Color.White)
                }

                Text(
                    text = "ServiceDigital",
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // Botón de usuario
                TextButton(
                    onClick = { showSidePanel = !showSidePanel }
                ) {
                    Text(userName, color = textColor, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de servicios con scroll
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(servicios) { servicio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = servicio.imagen),
                                contentDescription = servicio.titulo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                servicio.titulo,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                fontSize = 18.sp
                            )
                            Text(
                                servicio.descripcion,
                                color = Color.DarkGray,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { /* TODO: contactar servicio */ },
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Contactar servicio", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Panel lateral
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .align(Alignment.TopEnd)
                .offset(x = panelOffset)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Text(
                    text = "Hola, $userName",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Cerrar sesión",
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable {
                            showSidePanel = false
                            navController.navigate("login") {
                                popUpTo("catalog") { inclusive = true }
                            }
                        }
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { showSidePanel = false },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Cerrar panel", color = Color.White)
                }
            }
        }
    }
}
