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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.servicedigital.R
import com.example.servicedigital.view.ServiceViewModel


// Modelo del servicio
data class Servicio(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val imagen: Int,
    val contacto: String
)

@Composable
fun ServiceCatalogScreen(
    navController: NavController,
    userName: String = "",
    viewModel: ServiceViewModel = viewModel() // Aquí se inyecta correctamente
) {
    var showDialog by remember { mutableStateOf(false) }
    var contactoSeleccionado by remember { mutableStateOf("") }

    val backgroundColor = Color(0xFFE3F2FD)
    val buttonColor = Color(0xFF64B5F6)
    val textColor = Color(0xFF0D47A1)
    var showSidePanel by remember { mutableStateOf(false) }

    val panelOffset by animateDpAsState(
        targetValue = if (showSidePanel) 0.dp else 250.dp,
        label = "panelOffset"
    )

    // Esto usa los servicios guardados en el ViewModel
    val serviciosBase = listOf(
        Servicio(1, "Instalación de gas certificada", "Técnico autorizado SEC", R.drawable.placeholder, "gas.sec@gmail.com"),
        Servicio(2, "Certificación eléctrica", "Ingeniero en electricidad", R.drawable.placeholder, "987654321"),
        Servicio(3, "Mantención de calefón", "Servicio a domicilio", R.drawable.placeholder, "mantencioncalefon@gmail.com"),
        Servicio(4, "Revisión de red de gas", "Profesional con experiencia", R.drawable.placeholder, "912345678"),
        Servicio(5, "Certificado de instalación", "Entrega en 24 horas", R.drawable.placeholder, "certificados@tecnicos.cl"),
        Servicio(6, "Gasfitería general", "Reparaciones y certificaciones", R.drawable.placeholder, "999888777")
    )

// Mezclamos los servicios base con los agregados desde UploadService
    val servicios = remember { mutableStateListOf<Servicio>() }
    LaunchedEffect(Unit) {
        servicios.clear()
        servicios.addAll(serviciosBase + viewModel.servicios)
    }

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
                IconButton(onClick = {navController.navigate("uploadService")}) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8plus48),
                        contentDescription = "uploadService",
                        tint = Color(0xFF0D47A1),
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = {navController.navigate("inboxScreen")}) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8urgentmessage24),
                        contentDescription = "inboxScreen",
                        tint = Color(0xFF0D47A1),
                        modifier = Modifier.size(28.dp)
                    )
                }

                /*Button(
                    onClick = { navController.navigate("uploadService") },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Subir servicio", color = Color.White)
                }*/



                Text(
                    text = "ServiceDigital",
                    color = textColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )



                IconButton(onClick = {showSidePanel = !showSidePanel}) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8profile24),
                        contentDescription = "Usuario",
                        tint = Color(0xFF0D47A1),
                        modifier = Modifier.size(28.dp)
                    )
                }

                /*TextButton(

                    onClick = { showSidePanel = !showSidePanel }
                ) {
                    //Text(userName, color = textColor, fontSize = 16.sp)
                    Image()
                }*/
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

                            // Botón de contacto
                            Button(
                                onClick = {
                                    contactoSeleccionado = servicio.contacto
                                    showDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Contactar servicio", color = Color.White)
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                ContactDialog(
                    contacto = contactoSeleccionado,
                    onClose = { showDialog = false },
                    navController = navController
                )
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
                modifier = Modifier
                    .fillMaxSize()
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

@Composable
fun ContactDialog(contacto: String, onClose: () -> Unit, navController: NavController) {
    val clipboardManager = LocalClipboardManager.current
    val esCorreo = contacto.contains("@")

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Información de contacto") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(contacto, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(contacto))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64B5F6))
                ) {
                    Text(if (esCorreo) "Copiar correo" else "Copiar número", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onClose()
                        navController.navigate("chatScreen/${contacto}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Text("Enviar mensaje", color = Color.White)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Cerrar", color = Color.Gray)
            }
        }
    )
}