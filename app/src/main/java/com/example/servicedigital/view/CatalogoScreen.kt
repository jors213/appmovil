package com.example.servicedigital.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
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
import androidx.compose.material3.CardDefaults
import com.example.servicedigital.network.RetrofitClient
import com.example.servicedigital.ui.theme.ThemeManager


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
    viewModel: ServiceViewModel = viewModel(),
    chatViewModel: ChatViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var contactoSeleccionado by remember { mutableStateOf("") }

    val backgroundColor = MaterialTheme.colorScheme.background
    val buttonColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onBackground
    val cardColor = MaterialTheme.colorScheme.surface
    
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

    val serviciosApi = viewModel.servicios

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getServices()
            if (response.isSuccessful && response.body() != null) {
                val serviciosApi = response.body()!!.map {
                    Servicio(
                        id = it.id ?: 0,
                        titulo = it.titulo,
                        descripcion = it.descripcion,
                        imagen = R.drawable.placeholder,
                        contacto = it.contacto
                    )
                }
                viewModel.servicios.clear()
                viewModel.servicios.addAll(response.body()!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val serviciosCombinados = remember ( serviciosApi ){
        serviciosBase + serviciosApi.map {
            Servicio(
                id = it.id ?: 0,
                titulo = it.titulo,
                descripcion = it.descripcion,
                imagen = R.drawable.placeholder,
                contacto = it.contacto
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .statusBarsPadding()
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("uploadService") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8plus48),
                        contentDescription = "uploadService",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                IconButton(onClick = { navController.navigate("qrScanner/$userName") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconoscaner),
                        contentDescription = "Escanear QR",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }


                IconButton(onClick = { navController.navigate("inboxScreen/$userName") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8urgentmessage24),
                        contentDescription = "inboxScreen",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "ServiceDigital",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                IconButton(onClick = { showSidePanel = !showSidePanel }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8profile24),
                        contentDescription = "Usuario",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //LISTA DE SERVICIOS
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(serviciosCombinados) { servicio ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {

                            //placeholder temporal
                            Image(
                                painter = painterResource(id = R.drawable.placeholder),
                                contentDescription = servicio.titulo,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(servicio.titulo, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(servicio.descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    contactoSeleccionado = servicio.contacto
                                    showDialog = true
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Contactar servicio", color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    }
                }
            }

            if (showDialog) {
                ContactDialog(
                    contacto = contactoSeleccionado,
                    userName = userName,
                    onClose = { showDialog = false },
                    navController = navController,
                    chatViewModel = chatViewModel
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
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
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
                // Perfil Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                ) {
                     Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Hola,",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                        Text(
                            text = userName,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                     }
                     Spacer(modifier = Modifier.width(12.dp))
                     Icon(
                         painter = painterResource(id = R.drawable.icons8profile24),
                         contentDescription = null,
                         tint = MaterialTheme.colorScheme.primary,
                         modifier = Modifier.size(40.dp)
                     )
                }

                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                
                Spacer(modifier = Modifier.height(16.dp))

                // Switch de Tema
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { ThemeManager.isDarkTheme = !ThemeManager.isDarkTheme }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if(ThemeManager.isDarkTheme) "Modo Oscuro" else "Modo Claro",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = if (ThemeManager.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Cambiar tema",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Botón "Muestra tu Qr"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showSidePanel = false
                            navController.navigate("userQr/$userName")
                        }
                        .padding(vertical = 12.dp),
                     horizontalArrangement = Arrangement.End,
                     verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tu Código QR",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.iconoscaner),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Divider(color = MaterialTheme.colorScheme.outlineVariant)

                // Cerrar Sesión
                TextButton(
                    onClick = {
                        showSidePanel = false
                        navController.navigate("login") {
                            popUpTo("catalog") { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Cerrar sesión",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showSidePanel = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar panel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ContactDialog(contacto: String, userName: String, onClose: () -> Unit, navController: NavController, chatViewModel: ChatViewModel) {
    val clipboardManager = LocalClipboardManager.current
    val esCorreo = contacto.contains("@")

    AlertDialog(
        onDismissRequest = onClose,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Información de contacto", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(contacto, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(contacto))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(if (esCorreo) "Copiar correo" else "Copiar número", color = MaterialTheme.colorScheme.onSecondary)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onClose()
                        navController.navigate("chatScreen/${contacto}/${userName}")

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Enviar mensaje", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Cerrar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
